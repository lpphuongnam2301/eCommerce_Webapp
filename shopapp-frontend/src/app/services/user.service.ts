import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginDTO } from '../dtos/user/login.dto';
import { RegisterDTO } from '../dtos/user/register.dto';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiRegister = `${environment.apiBaseUrl}/users/register`;
  private apiLogin = `${environment.apiBaseUrl}/users/login`;
  private apiConfig = {
    headers: this.setHeaders(),
  }

  constructor(private http: HttpClient) 
  { 

  }
  private setHeaders(): HttpHeaders
  {
    return new HttpHeaders(
      {
        'Content-Type':'application/json'
      });
  }

  register(registerDTO: RegisterDTO):Observable<any>
  {
    return this.http.post(this.apiRegister, registerDTO, this.apiConfig);
  }

  login(loginDTO: LoginDTO): Observable<any>  
  {
    return this.http.post(this.apiLogin, loginDTO, this.apiConfig);
  }
}