import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Restaurant } from '../../models/restaurant.model';

@Component({
  selector: 'add-restaurant-dialog',
  templateUrl: './add-restaurant-dialog.component.html',
  styleUrls: ['./add-restaurant-dialog.component.scss']
})
export class AddRestaurantDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<AddRestaurantDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Restaurant) {}

    onFileSelected(event: any): void {
      const file: File = event.target.files[0];
      if (file) {
        this.data.file = file; // Ensure 'file' is added to your Restaurant interface
      }
    }
    
}
