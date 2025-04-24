export const AUTH_CONFIG = {
  token: localStorage.getItem('authToken') || '',
  setToken: (token: string) => {
    localStorage.setItem('authToken', token);
    AUTH_CONFIG.token = token;
  },
  clearToken: () => {
    localStorage.removeItem('authToken');
    AUTH_CONFIG.token = '';
  },
  getAuthHeader: () => ({
    Authorization: `Bearer ${AUTH_CONFIG.token}`,
    'Content-Type': 'application/json',
  }),
}; 