import http from './http-config';
import getToken from "./token-config";


/**
 * @description Get all user records
 */
export const getTraceabilityAll = async () => {
    return http.get(`/record/api/v1/all`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}

/**
 * @description Get all user records by user id
 * @param id user id
 */
export const getTraceabilityByUserUserId = async (id: number) => {
    return http.get(`/record/api/v1/` + id, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}


/**
 * @description Get all user records by date
 * @param beginning beginning date
 * @param finalDate final date
 */
export const getTraceabilityByDate = async (beginning, finalDate) => {
    return http.get(`/record/api/v1/date/` + beginning + "/" + finalDate, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}

/**
 * @description Get all user records by date and user id
 * @param beginning beginning date
 * @param finalDate final date
 * @param id user id
 */
export const getTraceabilityByDateUser = async (beginning, finalDate, id) => {
    return http.get(`/record/api/v1/date-and-user/` + beginning + "/" + finalDate + "/" + id, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}