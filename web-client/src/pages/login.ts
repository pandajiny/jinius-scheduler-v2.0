import {
  createUserWithEmailPassword,
  doLoginWithEmailAndPassword,
  checkAuthUser,
} from "../firebase/Auth";

import * as PATHS from "../constants/pathnames";

const $message = document.getElementById("login-message") as HTMLElement;

const updateMessage = (message: string) => {
  $message.innerHTML = message;
};

$message.addEventListener("click", (e: MouseEvent) => {
  console.log("hello");
  console.log(checkAuthUser());
});

const $emailInput = document.getElementById(
  "login-email-input"
) as HTMLInputElement;
const $passwordInput = document.getElementById(
  "login-password-input"
) as HTMLInputElement;

const $loginButton = document.getElementById(
  "login-button"
) as HTMLButtonElement;

$loginButton.addEventListener("click", (e: MouseEvent) => {
  doLogin();
});

const doLogin = () => {
  const email = $emailInput.value;
  const password = $passwordInput.value;
  updateMessage("loading...");
  doLoginWithEmailAndPassword({ email, password }).then((result) => {
    if (result.ok) {
      window.location.pathname = PATHS.MAIN_PAGE;
    } else {
      if (result.error != null) {
        updateMessage(result.error);
      }
    }
  });
};
