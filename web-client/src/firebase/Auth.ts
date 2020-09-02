import { firebaseApp } from "./Initialize";

type CreateUserResult = {
  ok: boolean;
  error?: string;
};

export const createUserWithEmailPassword = async (
  email: string,
  password: string
): Promise<CreateUserResult> => {
  return firebaseApp
    .auth()
    .createUserWithEmailAndPassword(email, password)
    .then((credential) => {
      console.log(`creating user successfull : ${credential.user?.email}`);
      const result: CreateUserResult = {
        ok: true,
      };
      return result;
    })
    .catch((reason) => {
      console.log(reason);
      const result: CreateUserResult = {
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

type LoginResult = {
  ok: boolean;
  error?: string;
};

export const doLoginWithEmailAndPassword = async (
  props: LoginRequest
): Promise<LoginResult> => {
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

export const checkAuthUser = async (): Promise<firebase.User> => {
  return new Promise((resolve, reject) => {
    // todo : set timeout
    firebaseApp.auth().onAuthStateChanged((user) => {
      if (user != null) {
        resolve(user);
      } else {
        reject("user not logged-in");
      }
    });
  });
};

type SignOutResult = {
  ok: boolean;
  error?: string;
};

export const doSignOut = (): Promise<SignOutResult> => {
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
