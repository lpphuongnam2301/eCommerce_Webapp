import { Component, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  @ViewChild(`registerForm`) registerForm!: NgForm;
  phone : String;
  password : String;
  retypePassword: String;
  fullName: String;
  address: String;
  isAccepted: boolean;
  dateOfBirth: Date;

  constructor(private http: HttpClient, private router: Router)
  {
    this.phone = '';
    this.password = '';
    this.retypePassword = '';
    this.fullName = '';
    this.address = '';
    this.isAccepted = false;
    this.dateOfBirth = new Date();
    this.dateOfBirth.setFullYear(this.dateOfBirth.getFullYear() - 18);
  }

  onPhoneChange()
  {
    console.log(`hello: ${this.phone}`);
  }

  register()
  {
      const apiUrl = "http://localhost:8088/api/v1/users/register";
      const headers = new HttpHeaders({'Content-type': 'application/json'});
      const registerData = {
        "fullname" : this.fullName,
        "phone_number": this.phone,
        "password" : this.password,
        "retype_password": this.retypePassword,
        "date_of_birth" : this.dateOfBirth,
        "address" : this.address,
        "facebook_account_id": 0,
        "google_account_id": 0,
        "role_id": 2
      }
      this.http.post(apiUrl, registerData, {headers})
      .subscribe({
        next: (response: any) => {
          if(response && (response.status === 200 || response.status === 201))
          {
            this.router.navigate(['/login']);
          }
        },
        complete: () => {

        },
        error: (error: any) => {
          alert(`Register fail, error: ${error.error}`);
        }
      });
  }

  checkRetypePassowrd()
  {
    if( this.password !== this.retypePassword)
    {
      this.registerForm.form.controls['retypePassword'].setErrors({'passwordMissmatch': true});
    } else {
      this.registerForm.form.controls[`retypePassword`].setErrors(null);
    }
  }

  checkAge()
  {
    if (this.dateOfBirth)
    {
      const today = new Date();
      const birthDate = new Date(this.dateOfBirth);
      let age = today.getFullYear() - birthDate.getFullYear();
      const month = today.getMonth() - birthDate.getMonth();
      if(month < 0 || (month === 0 && today.getDate() < birthDate.getDate()))
      {
        age--;
      }
      if(age < 18)
      {
        this.registerForm.form.controls['dateOfBirth'].setErrors({'invalidAge' : true});
      } else {
        this.registerForm.form.controls['dateOfBirth'].setErrors(null);
      }
    }
  }
}
