import http from './http-config';
import getToken from "./token-config";


/**
 * @description get user by id
 * @param id user id
 */
export const getUserId = async (id: number) => {
    return http.get(`/user/api/v1/` + id, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}

/**
 * @description enable user by id
 * @param id user id
 */
export const enableUser = async (id: number) => {
    return http.put(`/user/api/v1/enable/`, id, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}

/**
 * @description get all users
 */
export const getUsersAll = async () => {
    return http.get(`/user/api/v1/all`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}

/**
 * @description get all usernames
 */
export const getUsersAllNames = async () => {
    return http.get(`/user/api/v1/usernames`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}

/**
 * @description save user
 * @param user user object
 */
export const saveUser = async (user) => {
    return http.post(`/user/api/v1/save`, user, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}

/**
 * @description update user
 * @param user user object
 */
export const updateUser = async (user) => {
    return http.put(`/user/api/v1/update`, user, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}