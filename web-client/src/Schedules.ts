import { $scheduleInput, $scheduleList } from "./index";
import { createSchedule, addSchedulesListener } from "./firebase/Database";

export let scheduleList: string[] = [];

export const submitSchedule = () => {
  const content = $scheduleInput.value;
  if (content.length == 0) {
    return;
  }
  $scheduleInput.value = "";
  createSchedule(content);
};
