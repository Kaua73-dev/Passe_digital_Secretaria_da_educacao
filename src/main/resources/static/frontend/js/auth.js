window.Auth = {
  storageKey: 'passe_digital_jwt',
  apiBaseUrl: 'http://localhost:8080/auth',

  saveToken(token) {
    localStorage.setItem(this.storageKey, token);
  },

  getToken() {
    return localStorage.getItem(this.storageKey);
  },

  removeToken() {
    localStorage.removeItem(this.storageKey);
  },

  isAuthenticated() {
    return Boolean(this.getToken());
  },

  redirectToLogin() {
    window.location.href = '/frontend/html/user/login.html';
  },

  redirectToApp() {
    window.location.href = '/frontend/html/app.html';
  },

  redirectToDenied() {
    window.location.href = '/frontend/html/accessDenied.html';
  },

  requireAuth() {
    if (!this.isAuthenticated()) {
      this.redirectToLogin();
    }
  },

  decodeTokenPayload(token) {
    if (!token) {
      return null;
    }

    const parts = token.split('.');
    if (parts.length !== 3) {
      return null;
    }

    const base64 = parts[1].replace(/-/g, '+').replace(/_/g, '/');
    const padded = base64.padEnd(base64.length + ((4 - (base64.length % 4)) % 4), '=');

    try {
      const json = atob(padded);
      return JSON.parse(json);
    } catch (error) {
      return null;
    }
  },

  getUserEnum() {
    const payload = this.decodeTokenPayload(this.getToken());
    if (!payload) {
      return null;
    }

    if (payload.userEnum) {
      return payload.userEnum;
    }

    if (payload.role) {
      return payload.role;
    }

    if (Array.isArray(payload.roles) && payload.roles.length > 0) {
      return payload.roles[0];
    }

    return null;
  },

  requireRole(allowedRoles = []) {
    this.requireAuth();

    const userEnum = this.getUserEnum();
    if (!userEnum || !allowedRoles.includes(userEnum)) {
      this.redirectToDenied();
    }
  },

  async login(registration, password) {
    const payload = { registration, password };
    const response = await fetch(`${this.apiBaseUrl}/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(payload)
    });

    const data = await response.json().catch(() => ({}));

    if (!response.ok) {
      if (response.status === 401) {
        throw new Error('Credenciais inválidas.');
      }
      throw new Error(data?.message || 'Não foi possível efetuar login.');
    }

    if (!data.token) {
      throw new Error('Resposta inválida da API.');
    }

    this.saveToken(data.token);
    return data.token;
  },

  async fetchWithAuth(url, options = {}) {
    const token = this.getToken();
    const headers = new Headers(options.headers || {});

    if (token) {
      headers.set('Authorization', `Bearer ${token}`);
    }

    if (!headers.has('Content-Type')) {
      headers.set('Content-Type', 'application/json');
    }

    const response = await fetch(url, {
      ...options,
      headers,
    });

    if (response.status === 401) {
      this.removeToken();
      this.redirectToLogin();
      throw new Error('Sessão expirada ou não autorizada.');
    }

    return response;
  }
};
