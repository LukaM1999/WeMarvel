import {initializeApp} from 'firebase/app';
import {getAuth} from 'firebase/auth';
import {getStorage} from 'firebase/storage';
import {getFirestore} from 'firebase/firestore';
import {getDatabase} from 'firebase/database';

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
export const firestoreDb = getFirestore(firebaseApp)
export const realtimeDb = getDatabase(firebaseApp,
    'https://wemarvel-f6594-default-rtdb.europe-west1.firebasedatabase.app')
export const storage = getStorage(firebaseApp);