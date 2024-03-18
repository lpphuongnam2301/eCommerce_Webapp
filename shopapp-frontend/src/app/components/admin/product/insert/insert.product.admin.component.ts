import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OnInit } from '@angular/core';
import { InsertProductDTO } from '../../../../dtos/product/insert.product.dto';
import { Category } from '../../../../models/category';
import { CategoryService } from '../../../../services/category.service';
import { ProductService } from '../../../../services/product.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-insert.product.admin',
  templateUrl: './insert.product.admin.component.html',
  styleUrls: ['./insert.product.admin.component.scss'],
  standalone: true,
  imports: [   
    CommonModule,
    FormsModule,
  ]
})
export class InsertProductAdminComponent implements OnInit {
  insertProductDTO: InsertProductDTO = {
    name: '',
    price: 0,
    description: '',
    category_id: 1,
    images: []
  };
  categories: Category[] = []; 
  constructor(    
    private route: ActivatedRoute,
    private router: Router,
    private categoryService: CategoryService,    
    private productService: ProductService,    
  ) {
    
  } 
  ngOnInit() {
    this.getCategories(1, 100)
  } 
  getCategories(page: number, limit: number) {
    this.categoryService.getCategories(page, limit).subscribe({
      next: (categories: Category[]) => {
        debugger
        this.categories = categories;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        console.error('Error fetching categories:', error);
      }
    });
  }
  onFileChange(event: any) {
    const files = event.target.files;
    if (files.length > 5) {
      alert('Please select a maximum of 5 images.');
      return;
    }
    this.insertProductDTO.images = files;
  }

  insertProduct() {    
    this.productService.insertProduct(this.insertProductDTO).subscribe({
      next: (response) => {
        debugger
        if (this.insertProductDTO.images.length > 0) {
          const productId = response.id; 
          this.productService.uploadImages(productId, this.insertProductDTO.images).subscribe({
            next: (imageResponse) => {
              debugger           
              console.log('Images uploaded successfully:', imageResponse);
              this.router.navigate(['../'], { relativeTo: this.route });
            },
            error: (error) => {
              alert(error.error)
              console.error('Error uploading images:', error);
            }
          })          
        }
      },
      error: (error) => {
        debugger
        alert(error.error)
        console.error('Error inserting product:', error);
      }
    });    
  }
}
