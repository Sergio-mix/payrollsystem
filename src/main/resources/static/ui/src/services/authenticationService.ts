import http from './http-config';

const API = "/auth/api/v1"

/**
 * @description login user
 * @param authRequest user credentials
 */
export const userLogin = async (authRequest) => {
    return http.post(API + `/login`, authRequest);
}

export const verifyCode = async (id, code, email) => {
    return http.post(API + `/verify-code`, {id: id, code: code, email: email});
}

export const registerNewPassword = async (id, code, password, email) => {
    return http.put(API + `/verify-code-change-password`, {id: id, code: code, password: password, email: email});
}

export const restorePassword = async (username) => {
    return http.post(API + `/recovery-password`, {username: username});
}