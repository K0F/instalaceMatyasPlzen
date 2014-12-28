// ==================================================
// drawBlobsAndEdges()
// ==================================================
void drawBlobsAndEdges(boolean drawBlobs, boolean drawEdges)
{
  noFill();
  Blob b;
  EdgeVertex eA,eB;
  for (int n=0 ; n<theBlobDetection.getBlobNb() ; n++)
  {
    b=theBlobDetection.getBlob(n);
    if (b!=null)
    {
      // Edges
      if (drawEdges)
      {
        strokeWeight(3);
        stroke(0,255,0);
        for (int m=0;m<b.getEdgeNb();m++)
        {
          eA = b.getEdgeVertexA(m);
          eB = b.getEdgeVertexB(m);
          if (eA !=null && eB !=null)
            line(
              eA.x*width, eA.y*height, 
              eB.x*width, eB.y*height
              );
        }
      }

      // Blobs
      if (drawBlobs)
      {
        
        float XX = b.xMin*width + b.w*width / 2;
        float YY = b.yMin*height + b.h*height / 2;
        
        strokeWeight(1);
        stroke(255,0,0);
        fill(255,0,0);
        text(n,XX,YY);
         SendData(XX, YY, n, 5);
 noFill();
        rect(
          b.xMin*width,b.yMin*height,
          b.w*width,b.h*height
          );
      }

    }

      }
}
