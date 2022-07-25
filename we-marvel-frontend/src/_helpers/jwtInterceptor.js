import axios from "axios";
import {store} from "@/main";

export function jwtInterceptor(){
    axios.interceptors.request.use(async config => {
        if(!config.url.includes('localhost')) return config
        const token = store.getters.token;
        if (token) {
            config.headers.common['Authorization'] = `Bearer ${token}`;
        }
        return config;
    });
}
