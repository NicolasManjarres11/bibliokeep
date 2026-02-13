export interface AuthResponse {
  access_token: string; // JWT
  type: string;
}


export interface TokenInformation {
  sub: string;
  "user-id": string;
  roles: string[];
}