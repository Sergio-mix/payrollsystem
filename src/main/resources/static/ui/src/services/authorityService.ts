import http from './http-config';
import getToken from "./token-config";

/**
 * @description all data users
 */
export const getAllAuthority = async () => {
    return http.get(`/authority/api/v1/all`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}