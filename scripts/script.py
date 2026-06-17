import os
import re
from typing import List, Dict


class Entry:
    example_name: str
    module: str
    function: str
    param_cnt: int
    param_refinements_cnt: int
    param_constraints_expressed_cnt: int
    param_constraints_desired_cnt: int
    has_return_type: bool
    return_type_refinements_cnt: int
    return_type_constraints_expressed_cnt: int
    return_type_constraints_desired_cnt: int
    has_bug: bool
    reported: bool

    def uid(self):
        return f"{self.example_name}::{self.module}::{self.function}"

    def __str__(self):
        str = f"{self.uid()} p=(cnt={self.param_cnt},refinements={self.param_refinements_cnt},expressiveness={self.param_constraints_expressed_cnt}/{self.param_constraints_desired_cnt})"
        if self.has_return_type:
            str += f" r=({self.return_type_refinements_cnt},{self.return_type_constraints_expressed_cnt}/{self.return_type_constraints_desired_cnt}))"
        str += f" bug={self.has_bug} reported={self.reported}"
        return str


def load_entries(dir: str, is_java: bool) -> Dict[str, Entry]:
    entries = {}
    for top_level_subdir in os.listdir(dir):
        for (subdir_path, _, filenames) in os.walk(os.path.join(dir, top_level_subdir)):
            for filename in filenames:
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
                        entry.example_name = (os.path.normpath(subdir_path).split(os.sep)[-1] if is_java else top_level_subdir).lower()
                        (entry.module, entry.function) = [s.lower() for s in sp[0].split("::")]
                        sp = sp[1:]
                        m = re.search(r"p=\((\d*),(\d*),(\d*)/(\d*)\)", sp[0])
                        entry.param_cnt = int(m.group(1))
                        entry.param_refinements_cnt = int(m.group(2))
                        entry.param_constraints_expressed_cnt = int(m.group(3))
                        entry.param_constraints_desired_cnt = int(m.group(4))
                        sp = sp[1:]
                        assert sp[0].startswith("r="), raw_line
                        if sp[0][2:] == "none":
                            entry.has_return_type = False
                        else:
                            entry.has_return_type = True
                            m = re.search(r"r=\((\d*),(\d*)/(\d*)\)", sp[0])
                            entry.return_type_refinements_cnt = int(m.group(1))
                            entry.return_type_constraints_expressed_cnt = int(m.group(2))
                            entry.return_type_constraints_desired_cnt = int(m.group(3))
                        sp = sp[1:]
                        entry.has_bug = "bug" in sp
                        if entry.has_bug:
                            sp.remove("bug")
                        entry.reported = "reported" in sp
                        if entry.reported:
                            sp.remove("reported")
                        assert len(sp) == 0, f"unknown marker(s): {sp} (complete line is {raw_line})"
                        assert entry.uid() not in entries
                        entries[entry.uid()] = entry
                    except ValueError as e:
                        raise ValueError(e.args[0] + f" (complete line is {raw_line})")
                    except AttributeError as e:
                        raise ValueError(e.args[0] + f" (complete line is {raw_line})")
    return entries


def main():
    licorne_data = load_entries("../licorne", is_java=False)
    checker_framework_data = load_entries("../java-checker-framework", is_java=True)
    all_ids = dict()
    all_ids.update(licorne_data)
    all_ids.update(checker_framework_data)
    for uid, _ in all_ids.items():
        if not uid in licorne_data:
            print("missing in licorne data: ", uid)
        if not uid in checker_framework_data:
            print("missing in checker framework data: ", uid)



if __name__ == "__main__":
    main()
