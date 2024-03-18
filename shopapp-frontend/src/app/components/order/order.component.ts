import { Component, OnInit } from '@angular/core';
import { Product } from '../../models/product';
import { CartService } from '../../services/cart.service';
import { ProductService } from '../../services/product.service';
import { OrderService } from '../../services/order.service';
import { TokenService } from '../../services/token.service';
import { environment } from '../../environments/environment';
import { OrderDTO } from '../../dtos/order/order.dto';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Order } from '../../models/order';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  standalone: true,
  imports: [
    HeaderComponent, FooterComponent, FormsModule, CommonModule, ReactiveFormsModule
  ]
})
export class OrderComponent implements OnInit{
  orderForm: FormGroup; 
  cartItems: { product: Product, quantity: number }[] = [];
  couponCode: string = ''; 
  totalAmount: number = 0;
  orderData: OrderDTO = {
    user_id: 0, 
    fullname: '', 
    email: '', 
    phone_number: '', 
    address: '', 
    note: '',
    total_money: 0, 
    payment_method: 'cod', 
    shipping_method: 'express',
    coupon_code: '',
    cart_items: []
  };

  constructor(
    private cartService: CartService,
    private productService: ProductService,
    private orderService: OrderService,
    private tokenService: TokenService,
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router,
  ) {

    this.orderForm = this.formBuilder.group({
      fullname: ['hoàng xx', Validators.required], 
      email: ['hoang234@gmail.com', [Validators.email]], 
      phone_number: ['11445547', [Validators.required, Validators.minLength(6)]], 
      address: ['nhà x ngõ y', [Validators.required, Validators.minLength(5)]], 
      note: ['dễ vữ'],
      shipping_method: ['express'],
      payment_method: ['cod']
    });
  }
  
  ngOnInit(): void {  
    debugger
    this.orderData.user_id = this.tokenService.getUserId();    
    debugger
    const cart = this.cartService.getCart();
    const productIds = Array.from(cart.keys()); 
    debugger    
    if(productIds.length === 0) {
      return;
    }    
    this.productService.getProductsByIds(productIds).subscribe({
      next: (products) => {            
        debugger
        this.cartItems = productIds.map((productId) => {
          debugger
          const product = products.find((p) => p.id === productId);
          if (product) {
            product.thumbnail = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
          }          
          return {
            product: product!,
            quantity: cart.get(productId)!
          };
        });
        console.log('haha');
      },
      complete: () => {
        debugger;
        this.calculateTotal()
      },
      error: (error: any) => {
        debugger;
        console.error('Error fetching detail:', error);
      }
    });        
  }
  placeOrder() {
    debugger
    if (this.orderForm.valid) {
      this.orderData = {
        ...this.orderData,
        ...this.orderForm.value
      };
      this.orderData.cart_items = this.cartItems.map(cartItem => ({
        product_id: cartItem.product.id,
        quantity: cartItem.quantity
      }));
      this.orderData.total_money =  this.totalAmount;
      this.orderService.placeOrder(this.orderData).subscribe({
        next: (response:Order) => {
          debugger;          
          alert('Đặt hàng thành công');
          this.cartService.clearCart();
          this.router.navigate(['/']);
        },
        complete: () => {
          debugger;
          this.calculateTotal();
        },
        error: (error: any) => {
          debugger;
          alert(`Lỗi khi đặt hàng: ${error}`);
        },
      });
    } else {
      alert('Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.');
    }        
  }
     
  calculateTotal(): void {
      this.totalAmount = this.cartItems.reduce(
          (total, item) => total + item.product.price * item.quantity,
          0
      );
  }

  applyCoupon(): void {

  }
}
