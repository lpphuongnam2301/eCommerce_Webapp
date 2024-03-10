import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { LoginDTO } from '../dtos/user/login.dto';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  @ViewChild(`loginForm`) registerForm!: NgForm;
  phone : String;
  password : String;

  constructor(private router: Router, private userService: UserService)
  {
    this.phone = '';
    this.password = '';
  }

  onPhoneChange()
  {

  }

  login()
  {
      const loginDTO:LoginDTO = {
        "phone_number": this.phone,
        "password" : this.password
      }
      this.userService.login(loginDTO).subscribe({
        next: (response: any) => {
          if(response && (response.status === 200 || response.status === 201))
          {
            //this.router.navigate(['/home']);
            console.log("ok");
          }
        },
        complete: () => {

        },
        error: (error: any) => {
          alert(`Login fail, error: ${error.error.message}`);
        }
      });
  }
}
