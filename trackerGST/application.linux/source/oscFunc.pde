
//////////////////////////////////////////////////////////////////////////////////////

void SendData(float x, float y, int i, int totalId) {

  ///totalID is all Id’s tat I have in my blob array
  xpos = map(x, 0, width, 0, 1);
  ypos = map(y, 0, height, 0, 1);
  session_id = i;
  s_id = totalId;

  OscBundle blobsBundle = new OscBundle();
  OscMessage aliveMessage = new OscMessage("/tuio/2Dcur");
  aliveMessage.add("alive");
  aliveMessage.add(s_id);

  OscMessage setMessage = new OscMessage("/tuio/2Dcur");
  setMessage.add("set");
  setMessage.add(session_id);
  setMessage.add(xpos);
  setMessage.add(ypos);
  setMessage.add(xspeed);
  setMessage.add(yspeed);
  setMessage.add(maccel);

  currentFrame++;
  OscMessage frameMessage = new OscMessage("/tuio/2Dcur");
  frameMessage.add("fseq");

  frameMessage.add(currentFrame);

  blobsBundle.add(aliveMessage);

  blobsBundle.add(frameMessage);
  blobsBundle.add(setMessage);


  oscP5.send(blobsBundle);

  //rect(10, 10, 10, 10);
}



//////////////////////////////////////////////////////////////////////////////////////


void completeCursorMessage(int x, int y, int i, int totalId) {
  xpos = map(x, 0, width/2, 0, 1);
  ypos = map(y, 0, height/2, 0, 1);
  session_id = i;
  s_id = totalId;

  OscBundle oscBundle = new OscBundle();

  OscMessage frameMessage = new OscMessage("/tuio/2Dcur");
  frameMessage.add("fseq");
  frameMessage.add(-1);

  OscMessage aliveMessage = new OscMessage("/tuio/2Dcur");
  aliveMessage.add("alive");

  OscMessage setMessage = new OscMessage("/tuio/2Dcur");
  setMessage.add("set");
  setMessage.add(session_id);
  setMessage.add(xpos);
  setMessage.add(ypos);
  setMessage.add(xspeed);
  setMessage.add(yspeed);
  setMessage.add(maccel);

  currentFrame++;



  oscBundle.add(frameMessage);
  oscP5.send(oscBundle);
}


//////////////////////////////////////////////////////////////////////////////////////

void cursorDelete( int totalId) {
  s_id = totalId;

  OscBundle cursorBundle = new OscBundle();
  OscMessage aliveMessage = new OscMessage("/tuio/2Dcur");
  aliveMessage.add("alive");
  aliveMessage.add(s_id);

  currentFrame++;
  OscMessage frameMessage = new OscMessage("/tuio/2Dcur");
  frameMessage.add("fseq");
  frameMessage.add(currentFrame);

  cursorBundle.add(aliveMessage);
  cursorBundle.add(frameMessage);

  oscP5.send(cursorBundle);
} 



//////////////////////////////////////////////////////////////////////////////////////

