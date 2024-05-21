import { Restaurant } from "./restaurant.model";

export class UserDTO {
    id: number;
    username: string;
    email: string;
    role: string;
    password: string;
    restaurant?: String; // Optional, depending on your implementation

    constructor() {
        this.id = 0;
        this.username = '';
        this.email = '';
        this.role = '';
        this.password = '';
        // Initialize restaurant or other properties if needed
    }
}