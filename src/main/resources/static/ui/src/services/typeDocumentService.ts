import http from './http-config';
import getToken from "./token-config";


/**
 * @description all type documents
 */
export const getAllTypeDocument = async () => {
    return http.get(`/type-document/api/v1/all`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}