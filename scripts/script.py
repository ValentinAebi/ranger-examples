import csv
import os
import re
import sys
from typing import Dict, List, Any, Tuple


class Entry:
    example_name: str
    module: str
    function: str
    param_cnt: int = 0
    param_refinements_cnt: int = 0
    param_constraints_expressed_cnt: int = 0
    param_constraints_desired_cnt: int = 0
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

    def uid(self):
        return f"{self.example_name}::{self.module}::{self.function}"

    def __str__(self):
        return f"{self.uid()} p=(cnt={self.param_cnt},refinements={self.param_refinements_cnt},expressiveness={self.param_constraints_expressed_cnt}/{self.param_constraints_desired_cnt}) " + \
            f"r=({self.return_type_refinements_cnt},{self.return_type_constraints_expressed_cnt}/{self.return_type_constraints_desired_cnt})) " + \
            f" bug={self.has_bug} reported={self.reported} aux-annot=({self.auxiliary_annot_cnt},{self.auxiliary_refinements_cnt},{self.auxiliary_constraints_cnt}) bypass={self.bypass_cnt}"


def load_entries(dir: str, is_java: bool) -> Dict[str, Entry]:
    ext = ".java" if is_java else ".lic"
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
                            os.path.normpath(subdir_path).split(os.sep)[-1] if is_java else top_level_subdir).lower()
                        (entry.module, entry.function) = [s.lower() for s in sp[0].split("::")]
                        sp = sp[1:]
                        m = re.search(r"p=\((\d*),(\d*),(\d*)/(\d*)\)", sp[0])
                        entry.param_cnt = int(m.group(1))
                        entry.param_refinements_cnt = int(m.group(2))
                        entry.param_constraints_expressed_cnt = int(m.group(3))
                        entry.param_constraints_desired_cnt = int(m.group(4))
                        sp = sp[1:]
                        assert sp[0].startswith("r="), raw_line
                        if sp[0][2:] != "none":
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
        entry1 = data1[uid]
        entry2 = data2[uid]
        if entry1.param_cnt != entry2.param_cnt:
            print("difference in parameter count: ", uid, file=sys.stderr)
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


def cross_if_true(b: bool):
    return "x" if b else " "


def mk_lang_data(e: Entry) -> List[Any]:
    return [e.param_refinements_cnt, e.param_constraints_expressed_cnt, e.return_type_refinements_cnt,
            e.return_type_constraints_expressed_cnt, e.reported, e.auxiliary_annot_cnt,
            e.auxiliary_refinements_cnt, e.auxiliary_constraints_cnt, e.bypass_cnt]


def create_raw_table(licorne_data: dict[str, Entry], checker_framework_data: dict[str, Entry]) -> List[List[Any]]:
    general_headers = ["Program", "Module", "Function", "# param", "# desired constr (params)",
                              "# desired constr (return)", "Bug"]
    per_lang_headers = ["# ref (params)", "# expressed constr (params)", "# ref (return)",
                               "# expressed constr (return)", "Reported",
                               "# aux annot", "# aux ref", "# aux constr", "# bypass"]

    def mk_lang_header(lang_code: str):
        return [f"{lang_code}: {h}" for h in per_lang_headers]

    all_ids = list(licorne_data.keys())
    table = [general_headers + mk_lang_header("lic") + mk_lang_header("jcf")]
    for uid in all_ids:
        lic_entry = licorne_data[uid]
        jcf_entry = checker_framework_data[uid]
        table.append(
            [lic_entry.example_name, lic_entry.module, lic_entry.function, lic_entry.param_cnt,
             lic_entry.param_constraints_desired_cnt, lic_entry.return_type_constraints_desired_cnt, lic_entry.has_bug] \
            + mk_lang_data(lic_entry, "lic") + mk_lang_data(jcf_entry, "jcf")
        )
    return table


def create_examples_table(data: List[Tuple[str, Dict[str, Entry]]]) -> List[List[Any]]:
    general_headers = ["prog", "#p", "#p-cstr", "#r-cstr"]
    per_lang_headers = ["#p-ref", "#p-cstr", "#r-ref",
                        "#r-cstr", "TN", "FN", "FP", "TP", "#aux-an", "#aux-ref",
                        "#aux-cstr", "#bypass", "failed"]

    def mk_lang_header(lang_code: str):
        return [f"{lang_code}: {h}" for h in per_lang_headers]

    all_examples = list(set([e.example_name for (_, e) in (data[0])[1].items()]))
    all_examples.sort()
    headers = general_headers + [h for (lang_id, _) in data for h in mk_lang_header(lang_id)]
    table = [headers]
    for ex in all_examples:
        ex_general_data = [e for (_, e) in data[0][1].items() if e.example_name == ex]
        p = sum([e.param_cnt for e in ex_general_data])
        p_c = sum([e.param_constraints_desired_cnt for e in ex_general_data])
        r_c = sum([e.return_type_constraints_desired_cnt for e in ex_general_data])
        row = [ex, p, p_c, r_c]
        for lang_id, dic in data:
            ex_lang_data = [e for (_, e) in dic.items() if e.example_name == ex]
            p_ref = sum([e.param_refinements_cnt for e in ex_lang_data])
            p_c = sum([e.param_constraints_expressed_cnt for e in ex_lang_data])
            r_ref = sum([e.return_type_refinements_cnt for e in ex_lang_data])
            r_c = sum([e.return_type_constraints_expressed_cnt for e in ex_lang_data])
            tn = len([e for e in ex_lang_data if not e.has_bug and not e.reported])
            fn = len([e for e in ex_lang_data if e.has_bug and not e.reported])
            fp = len([e for e in ex_lang_data if not e.has_bug and e.reported])
            tp = len([e for e in ex_lang_data if e.has_bug and e.reported])
            aux_annot = sum([e.auxiliary_annot_cnt for e in ex_lang_data])
            aux_ref = sum([e.auxiliary_refinements_cnt for e in ex_lang_data])
            aux_c = sum([e.auxiliary_constraints_cnt for e in ex_lang_data])
            bypass = sum([e.bypass_cnt for e in ex_lang_data])
            failure = any([e.tool_failure for e in ex_lang_data])
            row += [p_ref, p_c, r_ref, r_c, tn, fn, fp, tp, aux_annot, aux_ref, aux_c, bypass, failure]
        table.append(row)
    return table


def main():
    licorne_data = load_entries("../licorne", is_java=False)
    checker_framework_data = load_entries("../java-checker-framework", is_java=True)
    liquid_java_data = load_entries("../liquid-java", is_java=True)
    compare(checker_framework_data, "jcf", licorne_data, "lic")
    compare(liquid_java_data, "lj", licorne_data, "lic")
    table = create_examples_table([("lic", licorne_data), ("jcf", checker_framework_data), ("lj", liquid_java_data)])
    os.makedirs("out", exist_ok=True)
    with open("./out/table.csv", "w", newline="") as f:
        wr = csv.writer(f)
        for row in table:
            wr.writerow(row)


if __name__ == "__main__":
    main()
