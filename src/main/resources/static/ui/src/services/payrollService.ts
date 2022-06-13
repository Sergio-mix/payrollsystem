import http from "./http-config";
import getToken from "./token-config";

export const savePayroll = async (file) => {
    let formData = new FormData();
    formData.append('file', file);
    return http.post(`/payroll/api/v1/save`, formData, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}

export const getPayrollAll = async () => {
    return http.get(`/payroll/api/v1/all`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}

export const getPayrollDataByPayrollId = async (id: number) => {
    return http.get(`/payroll/api/v1/data/${id}`, {
        headers: {
            'Authorization': `Bearer ${getToken()}`
        }
    });
}