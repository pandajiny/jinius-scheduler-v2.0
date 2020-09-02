import { firebaseApp } from "./Initialize";

const db = firebaseApp.firestore();

export const createSchedule = (content: string) => {
  console.log("create schedule");
  db.collection("test")
    .doc()
    .set({ content: content })
    .then(() => {
      console.log("successfully saved");
    })
    .catch((reason) => {
      console.log(reason);
    });
};
export const addSchedulesListener = (
  callback: (schedules: string[]) => void
) => {
  console.log("schedule listener added");
  db.collection("test").onSnapshot((snapshot) => {
    const schedules: string[] = [];
    snapshot.docs.map((document) => {
      if (typeof document.data().content == "string") {
        const schedule = (document.data().content as unknown) as string;
        schedules.push(schedule);
      }
    });
    callback(schedules);
  });
};
