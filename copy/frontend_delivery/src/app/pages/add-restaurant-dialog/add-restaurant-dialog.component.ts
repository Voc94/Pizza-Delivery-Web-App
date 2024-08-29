import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Restaurant } from '../../models/restaurant.model';

@Component({
  selector: 'add-restaurant-dialog',
  templateUrl: './add-restaurant-dialog.component.html',
  styleUrls: ['./add-restaurant-dialog.component.scss']
})
export class AddRestaurantDialogComponent {
  selectedFile: File | null = null;

  constructor(
    public dialogRef: MatDialogRef<AddRestaurantDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Restaurant) {}

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    if (this.selectedFile) {
      this.data.file = this.selectedFile;  // Ensure 'file' is added to your Restaurant interface
    }
  }

  onSubmit(): void {
    if (this.data) {
      this.dialogRef.close({ ...this.data, file: this.selectedFile });
    }
  }
}
