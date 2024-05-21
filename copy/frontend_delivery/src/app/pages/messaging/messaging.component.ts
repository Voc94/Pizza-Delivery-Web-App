import { Component } from '@angular/core';
import { WebSocketAPI } from '../../websocket.service';

@Component({
  selector: 'app-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: ['./messaging.component.scss']
})
export class MessagingComponent {
  webSocketAPI!: WebSocketAPI;
  isConnected: boolean = false;
  messages: string[] = [];
  name: string | undefined;

  ngOnInit() {
    this.webSocketAPI = new WebSocketAPI(this);
  }

  connect(){
    this.webSocketAPI._connect();
    this.isConnected = true;
  }

  disconnect(){
    this.webSocketAPI._disconnect();
    this.isConnected = false;
  }

  sendMessage(){
    this.webSocketAPI._send({ name: this.name });
  }

  handleMessage(jsonStr: string){
    const message = JSON.parse(jsonStr);
    const body = JSON.parse(message.body);
    this.messages.push(body.content); // Assuming 'content' holds the actual message to display
  }
}
