import axios from 'axios';

const API = 'http://localhost:8080/';
//const API = 'http://192.168.0.13:8080/';
//const API = 'http://150.136.87.96:8080/';

/**
 * @description axios config
 */
export default axios.create({
    baseURL: API,
    headers: {
        "Content-type": "application/json",
    }
});

