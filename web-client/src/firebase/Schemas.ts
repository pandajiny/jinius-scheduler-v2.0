export interface Note {
  uid: string;
  content: string;
  createdTime: firebase.firestore.Timestamp;
}
