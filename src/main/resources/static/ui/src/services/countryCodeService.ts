import http from './http-config';
import getToken from "./token-config";

/**
 * @description all country code
 */
export const getAllCountryCode = async () => {
    return http.get(`/country-code/api/v1/all`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}