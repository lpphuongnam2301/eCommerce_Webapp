import {
    IsString,
    IsNotEmpty,
    IsPhoneNumber,
    IsDate
} from 'class-validator';

export class RegisterDTO {
    @IsString()
    fullname: String;

    @IsPhoneNumber()
    @IsNotEmpty()
    phone_number: String;

    @IsString()
    @IsNotEmpty()
    password: String;

    @IsString()
    @IsNotEmpty()
    retype_password: String;

    @IsDate()
    date_of_birth: Date;

    @IsString()
    address: String;

    facebook_account_id: number = 0;
    google_account_id: number = 0;
    role_id: number = 2;

    constructor(data: any) 
    {
        this.fullname = data.fullname;
        this.phone_number = data.phone_number;
        this.password = data.password;
        this.retype_password = data.retype_password;
        this.date_of_birth = data.date_of_birth;
        this.address = data.address;
        this.facebook_account_id = data.facebook_account_id || 0;
        this.google_account_id = data.google_account_id || 0;
        this.role_id = data.role_id || 0;
    }
}