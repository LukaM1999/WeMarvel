import {initializeApp} from 'firebase/app';
import {getAuth} from 'firebase/auth';

const firebaseConfig = {
    apiKey: "AIzaSyDlji_5zMWzcvaor8L9msj9isY_oXTOR60",
    authDomain: "wemarvel-f6594.firebaseapp.com",
    projectId: "wemarvel-f6594",
    storageBucket: "wemarvel-f6594.appspot.com",
    messagingSenderId: "1076038087332",
    appId: "1:1076038087332:web:ce21bba1c07bd77b8bac31"
};

export const firebaseApp = initializeApp(firebaseConfig);
export const auth = getAuth(firebaseApp);