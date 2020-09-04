import { firebaseApp, firebaseInstance } from "./Initialize";

import { Note } from "./Schemas";

const db = firebaseApp.firestore();

export const getCurrentTimestamp = (): firebase.firestore.Timestamp => {
  return firebaseInstance.firestore.Timestamp.now();
};

//  About Schedules
const notesCollection = db.collection("notes");

export const createNote = (note: Note) => {
  console.log("create schedule");
  notesCollection
    .doc()
    .set(note)
    .then(() => {
      console.log("successfully saved");
    })
    .catch((reason) => {
      console.log(reason);
    });
};

export const addNotesUpdateListener = (
  uid: string,
  callback: (notes: Note[]) => void
) => {
  console.log("schedule listener added");
  notesCollection
    .where("uid", "==", uid)
    .orderBy("createdTime")
    .onSnapshot((snapshot) => {
      const notes: Note[] = [];
      snapshot.docs.map((document) => {
        if (typeof document.data().content == "string") {
          const schedule = (document.data() as unknown) as Note;
          notes.push(schedule);
        }
      });
      callback(notes);
    });
};
