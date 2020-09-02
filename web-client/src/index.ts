import { submitSchedule } from "./Schedules";
import { checkAuthUser, doSignOut } from "./firebase/Auth";
import { addSchedulesListener } from "./firebase/Database";

import * as PATHS from "./constants/pathnames";

// login
const $loginMessage = document.getElementById("login-message") as HTMLElement;

let isLoggedIn: boolean | null = null;

const $loginButton = document.getElementById(
  "login-button"
) as HTMLButtonElement;

// check user Auth
checkAuthUser()
  .then((user) => {
    isLoggedIn = true;
    updateLoginButton(isLoggedIn);
    $loginMessage.innerHTML = `Welcome,${user.email}!`;
  })
  .catch((reason) => {
    isLoggedIn = false;
    updateLoginButton(isLoggedIn);
    $loginMessage.innerHTML = `${reason}`;
  });

const updateLoginButton = (isLoggedIn: boolean) => {
  if (isLoggedIn) {
    $loginButton.innerHTML = "Sign-Out";
  } else {
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
    window.location.pathname = PATHS.LOGIN_PAGE;
  } else {
    doSignOut().then((result) => {
      if (result.ok) {
        // set Action
        isLoggedIn = false;
        updateLoginButton(isLoggedIn);
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
    submitSchedule();
  }
});

$scheduleInput.addEventListener("keypress", (e: KeyboardEvent) => {
  if (e.key == "Enter") {
    submitSchedule();
  }
});

// schedule list
export const $scheduleList = document.getElementById(
  "schedules-list"
) as HTMLUListElement;

addSchedulesListener((schedules) => {
  $scheduleList.innerHTML = "";
  schedules.map((schedule) => {
    const $item = document.createElement("li");
    const textNode = document.createTextNode(schedule);
    $item.appendChild(textNode);
    $scheduleList.appendChild($item);
  });
});
