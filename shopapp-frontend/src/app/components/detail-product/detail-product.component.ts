import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { CartService } from '../../services/cart.service';
import { CategoryService } from '../../services/category.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Product } from '../../models/product';
import { ProductImage } from '../../models/product.image';
import { environment } from '../../environments/environment';
import { HeaderComponent } from '../header/header.component';
import { FooterComponent } from '../footer/footer.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-detail-product',
  templateUrl: './detail-product.component.html',
  styleUrls: ['./detail-product.component.scss'],
  standalone: true,
  imports: [
    HeaderComponent, FooterComponent, FormsModule, CommonModule
  ]
})

export class DetailProductComponent implements OnInit {
  product?: Product;
  productId: number = 0;
  currentImageIndex: number = 0;
  quantity: number = 1;
  constructor(
    private productService: ProductService,
    private cartService: CartService,
    // private categoryService: CategoryService,
    // private router: Router,
      private activatedRoute: ActivatedRoute,
      private router: Router,
    ) {
      
    }
    ngOnInit() {   
      const idParam = this.activatedRoute.snapshot.paramMap.get('id');
      debugger
      //this.cartService.clearCart();

      if (idParam !== null) {
        this.productId = +idParam;
      }
      if (!isNaN(this.productId)) {
        this.productService.getDetailProduct(this.productId).subscribe({
          next: (response: any) => {            
            debugger
            if (response.product_images && response.product_images.length > 0) {
              response.product_images.forEach((product_image:ProductImage) => {
                product_image.image_url = `${environment.apiBaseUrl}/products/images/${product_image.image_url}`;
              });
            }            
            debugger
            this.product = response 
            this.showImage(0);
          },
          complete: () => {
            debugger;
          },
          error: (error: any) => {
            debugger;
            console.error('Error fetching detail:', error);
          }
        });    
      } else {
        console.error('Invalid productId:', idParam);
      }      
    }
    showImage(index: number): void {
      debugger
      if (this.product && this.product.product_images && 
          this.product.product_images.length > 0) {   
        if (index < 0) {
          index = 0;
        } else if (index >= this.product.product_images.length) {
          index = this.product.product_images.length - 1;
        }        
        this.currentImageIndex = index;
      }
    }
    thumbnailClick(index: number) {
      debugger
      this.currentImageIndex = index;
    }  
    nextImage(): void {
      debugger
      this.showImage(this.currentImageIndex + 1);
    }
  
    previousImage(): void {
      debugger
      this.showImage(this.currentImageIndex - 1);
    }      
    addToCart(): void {
      debugger
      if (this.product) {
        this.cartService.addToCart(this.product.id, this.quantity);
      } else {
        console.error('Không thể thêm sản phẩm vào giỏ hàng vì product là null.');
      }
    }    
        
    increaseQuantity(): void {
      this.quantity++;
    }
    
    decreaseQuantity(): void {
      if (this.quantity > 1) {
        this.quantity--;
      }
    }
    
    buyNow(): void {      
      this.router.navigate(['/orders']);
    }    
}
