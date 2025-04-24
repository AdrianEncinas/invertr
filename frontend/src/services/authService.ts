import { AUTH_CONFIG } from '../config/auth';

interface LoginResponse {
  token: string;
  type: string;
}

interface LoginRequest {
  username: string;
  password: string;
}

export const login = async (username: string, password: string): Promise<LoginResponse> => {
  try {
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password } as LoginRequest),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Credenciales inválidas');
    }

    const data: LoginResponse = await response.json();
    AUTH_CONFIG.setToken(data.token);
    return data;
  } catch (error) {
    if (error instanceof Error) {
      throw error;
    }
    throw new Error('Error al iniciar sesión');
  }
};

export const logout = () => {
  AUTH_CONFIG.clearToken();
};

export const isAuthenticated = (): boolean => {
  return !!AUTH_CONFIG.token;
}; 