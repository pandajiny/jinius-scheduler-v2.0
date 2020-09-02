import * as firebase from "firebase/app";
import "firebase/auth";
import "firebase/firestore";

import { firebaseConfig } from "../../env/firebaseconfig";

export const firebaseApp = firebase.initializeApp(firebaseConfig);
