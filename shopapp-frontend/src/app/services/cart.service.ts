import { Injectable } from '@angular/core';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})

export class CartService {
  private cart: Map<number, number> = new Map(); 

  constructor() { 
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      this.cart = new Map(JSON.parse(storedCart));      
    }
  }

  addToCart(productId: number, quantity: number = 1): void {
    debugger
    if (this.cart.has(productId)) {
      this.cart.set(productId, this.cart.get(productId)! + quantity);
    } else {
      this.cart.set(productId, quantity);
    }
    this.saveCartToLocalStorage();
  }
  
  getCart(): Map<number, number> {
    return this.cart;
  }
  private saveCartToLocalStorage(): void {
    debugger
    localStorage.setItem('cart', JSON.stringify(Array.from(this.cart.entries())));
  }  
  clearCart(): void {
    this.cart.clear(); 
    this.saveCartToLocalStorage(); 
  }
}
