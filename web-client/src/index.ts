import { getAuthUser, doSignOut } from "./firebase/Auth";
import {
  addNotesUpdateListener,
  getCurrentTimestamp,
  createNote,
} from "./firebase/Database";
import { Note } from "./firebase/Schemas";

import { goLoginPage } from "./Navigator";

// login
let isLoggedIn: boolean | null = null;

let currentUser: firebase.User | null = null;

// check user Auth and update login state
const checkUserLogin = async () => {
  const user = await getAuthUser();
  if (user != null) {
    currentUser = user;
    isLoggedIn = true;
  } else {
    isLoggedIn = false;
  }
  updateLoginView(isLoggedIn);
};

checkUserLogin();

const $loginMessage = document.getElementById("login-message") as HTMLElement;

const $loginButton = document.getElementById(
  "login-button"
) as HTMLButtonElement;

const updateLoginView = (isLoggedIn: boolean) => {
  if (isLoggedIn && currentUser != null) {
    // login
    $loginMessage.innerHTML = `Welcome,${currentUser.email}!`;
    $loginButton.innerHTML = "Sign-Out";
    setNotesListListener(currentUser.uid);
  } else {
    // no login
    $loginMessage.innerHTML = `Please Login first`;
    $loginButton.innerHTML = "Go Login Page";
  }
};

$loginButton.addEventListener("click", (e: MouseEvent) => {
  if (isLoggedIn == null) {
    return;
  }
  loginButtonAction(isLoggedIn);
});

const loginButtonAction = (isLoggedIn: boolean) => {
  if (!isLoggedIn) {
    goLoginPage();
  } else {
    doSignOut().then((result) => {
      if (result.ok) {
        // set Action
        isLoggedIn = false;
        updateLoginView(isLoggedIn);
      }
    });
  }
};

// schedule input form
export const $scheduleInput = document.getElementById(
  "schedule-input"
) as HTMLInputElement;

export const $inputEnterButton = document.getElementById(
  "schedule-submit-button"
) as HTMLButtonElement;

$inputEnterButton?.addEventListener("click", (e: MouseEvent) => {
  const input = $scheduleInput.value;
  if (typeof input == "string" && input.length > 0) {
    submitNote();
  }
});

$scheduleInput.addEventListener("keypress", (e: KeyboardEvent) => {
  if (e.key == "Enter") {
    submitNote();
  }
});

const submitNote = async () => {
  const uid = await (await getAuthUser()).uid;
  const content = $scheduleInput.value;
  const createdTime = getCurrentTimestamp();

  if (content.length == 0) {
    return;
  }

  const note: Note = {
    uid,
    content,
    createdTime,
  };

  $scheduleInput.value = "";

  createNote(note);
};

// schedule list
export const $scheduleList = document.getElementById(
  "schedules-list"
) as HTMLUListElement;

const setNotesListListener = (uid: string) => {
  addNotesUpdateListener(uid, (notes) => {
    $scheduleList.innerHTML = "";
    notes.map((note) => {
      const $item = document.createElement("li");
      const textNode = document.createTextNode(note.content);
      $item.appendChild(textNode);
      $scheduleList.appendChild($item);
    });
  });
};
