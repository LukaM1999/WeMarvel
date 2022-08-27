import axios from "axios";
import {store} from "@/main";

export function jwtInterceptor(){
    axios.interceptors.request.use(async config => {
        if(!config.url.includes('localhost')) return config;
        const refreshToken = store.getters.user?.stsTokenManager?.refreshToken;
        let newToken;
        if(refreshToken) {
            const {data} = await axios.post('https://securetoken.googleapis.com/v1/token?key=AIzaSyDlji_5zMWzcvaor8L9msj9isY_oXTOR60', {
                grant_type: 'refresh_token',
                refresh_token: refreshToken
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            newToken = data.id_token;
        }
        const token = store.getters.token;
        if (newToken || token) {
            config.headers.common['Authorization'] = `Bearer ${newToken ? newToken : token}`;
        }
        return config;
    });
}
