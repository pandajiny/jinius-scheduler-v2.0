import { firebaseApp } from "./Initialize";

interface AuthActionResult {
  ok: boolean;
  error?: string;
}
export const createUserWithEmailPassword = async (
  email: string,
  password: string
): Promise<AuthActionResult> => {
  return firebaseApp
    .auth()
    .createUserWithEmailAndPassword(email, password)
    .then((credential) => {
      console.log(`creating user successfull : ${credential.user?.email}`);
      const result: AuthActionResult = {
        ok: true,
      };
      return result;
    })
    .catch((reason) => {
      console.log(reason);
      const result: AuthActionResult = {
        ok: false,
        error: reason,
      };
      return result;
    });
};

type LoginRequest = {
  email: string;
  password: string;
};

export const doLoginWithEmailAndPassword = async (
  props: LoginRequest
): Promise<AuthActionResult> => {
  const { email, password } = props;
  return firebaseApp
    .auth()
    .signInWithEmailAndPassword(email, password)
    .then((credential) => {
      console.log(`login success : ${credential.user?.email}`);
      return {
        ok: true,
      };
    })
    .catch((error) => {
      return {
        ok: false,
        error: error,
      };
    });
};

export const getAuthUser = async (): Promise<firebase.User | null> => {
  return new Promise((resolve) => {
    // todo : set timeout
    firebaseApp.auth().onAuthStateChanged((user) => {
      if (user != null) {
        resolve(user);
      } else {
        resolve(null);
      }
    });
  });
};

export const doSignOut = (): Promise<AuthActionResult> => {
  return firebaseApp
    .auth()
    .signOut()
    .then(() => {
      return {
        ok: true,
      };
    })
    .catch((reason) => {
      return {
        ok: false,
        error: reason,
      };
    });
};
