export interface AuthResponse {
  accessToken: string; // JWT
  refreshToken: string;
  tokenType?: string; // Usually "Bearer"
}
