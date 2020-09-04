import { doLoginWithEmailAndPassword, getAuthUser } from "../firebase/Auth";

import { goHome, goSignUpPage } from "../Navigator";

// back to main
const $mainButton = document.getElementById("main-button") as HTMLButtonElement;
$mainButton?.addEventListener("click", () => {
  goHome();
});

// check login state
const checkAuthUser = async () => {
  const user = await getAuthUser();
  if (user != null) {
    goHome();
  }
};

checkAuthUser();

const $message = document.getElementById("login-message") as HTMLElement;

const updateMessage = (message: string) => {
  $message.innerHTML = message;
};

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
      goHome();
    } else {
      if (result.error != null) {
        updateMessage(result.error);
      }
    }
  });
};

const $signupButton = document.getElementById(
  "signup-button"
) as HTMLButtonElement;

$signupButton.addEventListener("click", () => {
  goSignUpPage();
});
