import csv
import os
import re
import sys
from typing import Dict, List, Any, Tuple, Final

hg_group = "group"
hg_prog = "prog"
hg_sig_type_cnt = "std"
hg_sig_cstr_cnt = "csr"

hs_failed = "fail"

hs_sig_ref_cnt = "ref"
hs_sig_cstr_cnt = "csr"

hs_aux_annot_cnt = "aux"
hs_aux_ref_cnt = "auxr"
hs_bypass_cnt = "cas"

hs_tn_cnt = "tn"
hs_fn_cnt = "fn"
hs_fp_cnt = "fp"
hs_tp_cnt = "tp"

licorne_code = "R"
checker_code = "C"
liquid_java_code = "L"
scala_code = "S"

liquid_java_excluded_examples = {"ArrayMap", "FilterLessThan"}

groups = {
    "rng": ["Datetime", "FilterLessThan", "MaxPos", "Motivation"],
    "amap": ["Arraymap"],
    "sort": ["Sorting"],
    "ic": ["ic4", "ic5", "ic7", "ic9"],
    "lj": ["lj1", "lj2", "lj3", "lj4"]
}

general_headers: Final = [hg_group, hg_prog, hg_sig_type_cnt, hg_sig_cstr_cnt]
per_lang_headers: Final = [
    (hs_failed, {liquid_java_code}),
    (hs_sig_ref_cnt, {licorne_code, checker_code, liquid_java_code}),
    (hs_sig_cstr_cnt, {licorne_code, checker_code, liquid_java_code}),
    (hs_aux_annot_cnt, {licorne_code, checker_code, liquid_java_code, scala_code}),
    (hs_aux_ref_cnt, {licorne_code, checker_code}),
    (hs_bypass_cnt, {licorne_code, checker_code}),
    (hs_tn_cnt, {licorne_code, checker_code, liquid_java_code}),
    (hs_fn_cnt, {licorne_code, checker_code, liquid_java_code}),
    (hs_fp_cnt, {licorne_code, checker_code, liquid_java_code}),
    (hs_tp_cnt, {licorne_code, checker_code, liquid_java_code}),
]

excluded_headers = {hs_failed}


class Entry:
    example_name: str
    module: str
    function: str
    param_cnt: int = 0
    param_refinements_cnt: int = 0
    param_constraints_expressed_cnt: int = 0
    param_constraints_desired_cnt: int = 0
    ret_cnt: int = 0
    return_type_refinements_cnt: int = 0
    return_type_constraints_expressed_cnt: int = 0
    return_type_constraints_desired_cnt: int = 0
    has_bug: bool = False
    reported: bool = False
    tool_failure: bool = False
    auxiliary_annot_cnt: int = 0
    auxiliary_refinements_cnt: int = 0
    auxiliary_constraints_cnt: int = 0
    bypass_cnt: int = 0
    is_scala: bool = False

    def uid(self):
        return f"{self.example_name}::{self.module}::{self.function}"

    def __str__(self):
        return f"{self.uid()} p=(cnt={self.param_cnt},refinements={self.param_refinements_cnt},expressiveness={self.param_constraints_expressed_cnt}/{self.param_constraints_desired_cnt}) " + \
            f"r=({self.return_type_refinements_cnt},{self.return_type_constraints_expressed_cnt}/{self.return_type_constraints_desired_cnt})) " + \
            f" bug={self.has_bug} reported={self.reported} aux-annot=({self.auxiliary_annot_cnt},{self.auxiliary_refinements_cnt},{self.auxiliary_constraints_cnt}) bypass={self.bypass_cnt}"


def load_entries(dir: str, ext: str) -> Dict[str, Entry]:
    entries = {}
    for top_level_subdir in os.listdir(dir):
        for (subdir_path, _, filenames) in os.walk(os.path.join(dir, top_level_subdir)):
            for filename in filenames:
                if not filename.endswith(ext):
                    continue
                for raw_line in open(os.path.join(subdir_path, filename)):
                    try:
                        line = raw_line.replace("\n", "").replace("\r", "").lower()
                        if not "//>" in line:
                            continue
                        sp = line.split("//>")
                        assert len(sp) == 2, raw_line
                        if "--" in sp[1]:
                            sp[1] = sp[1].split("--")[0]
                        sp = sp[1].split(' ')[1:]
                        sp = list(filter(lambda x: len(x) > 0, sp))
                        entry = Entry()
                        entry.example_name = (
                            (top_level_subdir if ext == ".lic" else os.path.normpath(subdir_path).split(os.sep)[-1])
                            .lower())
                        (entry.module, entry.function) = [s.lower() for s in sp[0].split("::")]
                        sp = sp[1:]
                        if ext == ".scala":
                            assert len(sp) <= 1, raw_line
                            if len(sp) == 1:
                                entry.is_scala = True
                                m = re.search(r"aux-annot=(\d*)", sp[0])
                                entry.auxiliary_annot_cnt = int(m.group(1))
                                sp = sp[1:]
                        else:
                            m = re.search(r"p=\((\d*),(\d*),(\d*)/(\d*)\)", sp[0])
                            entry.param_cnt = int(m.group(1))
                            entry.param_refinements_cnt = int(m.group(2))
                            entry.param_constraints_expressed_cnt = int(m.group(3))
                            entry.param_constraints_desired_cnt = int(m.group(4))
                            sp = sp[1:]
                            assert sp[0].startswith("r="), raw_line
                            if sp[0][2:] != "none":
                                entry.ret_cnt = 1
                                m = re.search(r"r=\((\d*),(\d*)/(\d*)\)", sp[0])
                                entry.return_type_refinements_cnt = int(m.group(1))
                                entry.return_type_constraints_expressed_cnt = int(m.group(2))
                                entry.return_type_constraints_desired_cnt = int(m.group(3))
                            sp = sp[1:]
                            aux_annot_tags = [t for t in sp if t.startswith("aux-annot")]
                            assert len(aux_annot_tags) <= 1
                            if len(aux_annot_tags) > 0:
                                m = re.search(r"aux-annot=\((\d*),(\d*),(\d*)\)", aux_annot_tags[0])
                                entry.auxiliary_annot_cnt = int(m.group(1))
                                entry.auxiliary_refinements_cnt = int(m.group(2))
                                entry.auxiliary_constraints_cnt = int(m.group(3))
                            bypass_tags = [t for t in sp if t.startswith("bypass")]
                            assert len(bypass_tags) <= 1, "too many bypass tags"
                            if len(bypass_tags) > 0:
                                m = re.search(r"bypass=(\d*)", bypass_tags[0])
                                entry.bypass_cnt = int(m.group(1))
                            sp = [t for t in sp if not t.startswith("aux-annot") and not t.startswith("bypass")]
                            entry.has_bug = "bug" in sp
                            if entry.has_bug:
                                sp.remove("bug")
                            entry.reported = "reported" in sp
                            if entry.reported:
                                sp.remove("reported")
                            entry.tool_failure = "fail" in sp
                            if entry.tool_failure:
                                sp.remove("fail")
                        assert len(sp) == 0, f"unknown marker(s): {sp}"
                        assert entry.uid() not in entries, "duplicate uid"
                        entries[entry.uid()] = entry
                    except Exception as e:
                        if len(e.args) > 0:
                            raise Exception(e.args[0] + f" (complete line is {raw_line})")
                        else:
                            raise e
    return entries


def compare(data1: dict[str, Entry], code1: str, data2: dict[str, Entry], code2: str):
    print(f"comparing {code1} and {code2}:", file=sys.stderr)
    all_ids = dict()
    all_ids.update(data1)
    all_ids.update(data2)
    diff_cnt = 0
    for uid, _ in all_ids.items():
        if not uid in data1:
            print(f"missing in {code1} data: ", uid)
            continue
        if not uid in data2:
            print(f"missing in {code2} data: ", uid)
            continue
        if code1 == scala_code or code2 == scala_code:
            continue
        entry1 = data1[uid]
        entry2 = data2[uid]
        if entry1.param_cnt != entry2.param_cnt:
            print("difference in parameter count: ", uid, file=sys.stderr)
            diff_cnt += 1
        if entry1.ret_cnt != entry2.ret_cnt:
            print("difference in return count: ", uid, file=sys.stderr)
            diff_cnt += 1
        if entry1.param_constraints_desired_cnt != entry2.param_constraints_desired_cnt:
            print("difference in desired parameter constraints: ", uid, file=sys.stderr)
            diff_cnt += 1
        if entry1.return_type_constraints_desired_cnt != entry2.return_type_constraints_desired_cnt:
            print("difference in desired return constraints: ", uid, file=sys.stderr)
            diff_cnt += 1
        if entry1.has_bug != entry2.has_bug:
            print("difference in bug status: ", uid, file=sys.stderr)
            diff_cnt += 1
    print(f"{diff_cnt} differences", file=sys.stderr)


def textsc(s: str) -> str:
    return "\\textsc{" + s + "}"


def mk_lang_data(e: Entry) -> List[Any]:
    return [e.param_refinements_cnt, e.param_constraints_expressed_cnt, e.return_type_refinements_cnt,
            e.return_type_constraints_expressed_cnt, e.reported, e.auxiliary_annot_cnt,
            e.auxiliary_refinements_cnt, e.auxiliary_constraints_cnt, e.bypass_cnt]


def create_per_example_dict(data: List[Tuple[str, Dict[str, Entry]]]) \
        -> Dict[str, Tuple[Dict[str, Any], Dict[str, Dict[str, Any]]]]:
    per_example: Dict[str, Tuple[Dict[str, Any], Dict[str, Dict[str, int]]]] = {}  # example -> metric -> language
    for group_id, group in groups.items():
        for ex_full_case in group:
            ex_low_case = ex_full_case.lower()
            ex_general_data = [e for (_, e) in data[0][1].items() if e.example_name == ex_low_case]
            group = group_id
            example_general_metrics = {
                hg_group: group,
                hg_prog: ex_full_case,
                hg_sig_type_cnt: sum([e.param_cnt + e.ret_cnt for e in ex_general_data]),
                hg_sig_cstr_cnt: sum(
                    [e.param_constraints_desired_cnt + e.return_type_constraints_desired_cnt for e in ex_general_data]),
            }
            example_per_lang_metrics: Dict[str, Dict[str, Any]] = dict([(h, {}) for h, _ in per_lang_headers])
            for lang_id, dic in data:
                if lang_id == liquid_java_code and ex_full_case in liquid_java_excluded_examples:
                    for h, _ in per_lang_headers:
                        example_per_lang_metrics[h][lang_id] = ""
                else:
                    ex_lang_data = [e for (_, e) in dic.items() if e.example_name == ex_low_case]
                    example_per_lang_metrics[hs_sig_ref_cnt][lang_id] = sum(
                        [e.param_refinements_cnt + e.return_type_refinements_cnt for e in ex_lang_data])
                    example_per_lang_metrics[hs_sig_cstr_cnt][lang_id] = sum(
                        [e.param_constraints_expressed_cnt + e.return_type_constraints_expressed_cnt for e in
                         ex_lang_data])
                    tn = len([e for e in ex_lang_data if not e.has_bug and not e.reported])
                    fn = len([e for e in ex_lang_data if e.has_bug and not e.reported])
                    fp = len([e for e in ex_lang_data if not e.has_bug and e.reported])
                    tp = len([e for e in ex_lang_data if e.has_bug and e.reported])
                    example_per_lang_metrics[hs_tn_cnt][lang_id] = tn
                    example_per_lang_metrics[hs_fn_cnt][lang_id] = fn
                    example_per_lang_metrics[hs_fp_cnt][lang_id] = fp
                    example_per_lang_metrics[hs_tp_cnt][lang_id] = tp
                    example_per_lang_metrics[hs_aux_annot_cnt][lang_id] = sum(
                        [e.auxiliary_annot_cnt for e in ex_lang_data])
                    example_per_lang_metrics[hs_aux_ref_cnt][lang_id] = sum(
                        [e.auxiliary_refinements_cnt for e in ex_lang_data])
                    example_per_lang_metrics[hs_bypass_cnt][lang_id] = sum([e.bypass_cnt for e in ex_lang_data])
                    example_per_lang_metrics[hs_failed][lang_id] = any([e.tool_failure for e in ex_lang_data])
            per_example[ex_full_case] = (example_general_metrics, example_per_lang_metrics)
    return per_example


def create_table(all_data: Dict[str, Tuple[Dict[str, Any], Dict[str, Dict[str, int]]]], languages: List[str]) \
        -> List[List[str]]:
    table = []
    for _, group in groups.items():
        for ex in group:
            general_metrics, per_lang_metrics = all_data[ex]
            row = [general_metrics[h] for h in general_headers]
            for h, h_langs in per_lang_headers:
                if h in excluded_headers:
                    continue
                for lang_id in languages:
                    if lang_id not in h_langs:
                        continue
                    row.append("F" if per_lang_metrics[hs_failed][lang_id] else per_lang_metrics[h][lang_id])
            table.append(row)
    return table


def write_csv(table: List[List[str]], languages: List[str]) -> None:
    os.makedirs("out", exist_ok=True)
    with open("./out/table.csv", "w", newline="") as f:
        wr = csv.writer(f)
        header = general_headers + [f"{h} ({l})" for h, h_langs in per_lang_headers for l in
                                    languages if l in h_langs and not h in excluded_headers]
        wr.writerow(header)
        for row in table:
            wr.writerow(row)


def mk_latex_table(table: List[List[str]], languages: List[str]) -> str:
    col_settings = "| "
    for _ in general_headers:
        col_settings += "c "
    for h, h_langs in per_lang_headers:
        if h not in excluded_headers:
            col_settings += "| "
            for _ in range(len(h_langs)):
                col_settings += "c "
    col_settings += "|"
    rs = "\\begin{tabular}{ " + col_settings + " }\n"
    rs += "\\toprule\n"
    header_1 = ([textsc(h) for h in general_headers]
                + ["\\multicolumn{" + str(len(h_langs)) + "}{c}{" + textsc(h) + "}" for h, h_langs in per_lang_headers
                   if h not in excluded_headers])
    header_2 = ["" for _ in general_headers] + [textsc(l) for h, h_langs in per_lang_headers for l in
                                                languages if l in h_langs and h not in excluded_headers]

    def mk_row(row: List[Any]):
        row_str = ""
        for i, cell in enumerate(row):
            if i > 0:
                row_str += " & "
            cell = str(cell)
            if i == 0 and cell.lower() != "total":
                cell = textsc(cell)
            row_str += cell
        row_str += " \\\\\n"
        return row_str

    rs += mk_row(header_1)
    rs += mk_row(header_2)
    rs += "\\midrule\n"
    for i, row in enumerate(table):
        if i == len(table) - 1:
            rs += "\\midrule\n"
        rs += mk_row(row)
    rs += "\\midrule\n"
    rs += "\\bottomrule\n"
    rs += "\\end{tabular}"
    return rs


def add_sum_row(table: List[List[str]]):
    n_cols = len(table[0])
    sums: List[Any] = [0 for _ in range(n_cols)]
    for row in table:
        for i, cell in enumerate(row):
            if sums[i] is not None and type(cell) == int:
                sums[i] += int(cell)
            else:
                sums[i] = None
    table.append(["" if s is None else str(s) for s in sums])
    table[-1][0] = "Total"


def main():
    licorne_data = load_entries("../licorne", ext=".lic")
    checker_framework_data = load_entries("../java-checker-framework", ext=".java")
    liquid_java_data = load_entries("../liquid-java", ext=".java")
    scala_data = load_entries("../scala", ext=".scala")
    compare(checker_framework_data, checker_code, licorne_data, licorne_code)
    compare(liquid_java_data, liquid_java_code, licorne_data, licorne_code)
    compare(scala_data, scala_code, licorne_data, licorne_code)
    per_ex = create_per_example_dict(
        [(licorne_code, licorne_data), (checker_code, checker_framework_data), (liquid_java_code, liquid_java_data),
         (scala_code, scala_data)])
    languages = [licorne_code, checker_code, liquid_java_code, scala_code]
    table = create_table(per_ex, languages)
    add_sum_row(table)
    print("\n\n")
    print(mk_latex_table(table, languages))
    print("\n\n")
    write_csv(table, languages)


if __name__ == "__main__":
    main()
