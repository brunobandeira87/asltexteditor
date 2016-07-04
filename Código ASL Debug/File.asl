 <- // calculate the area of each quadrant and remember them
 	.print("Defining quadrants for ", W, "x", H, " simulation", S);
 	+quad(s,1,0,0,W div 2 - 1, H div 2 - 1);

+!allocate_miner(Gold);
 <- .findall(op(Dist, A), big(Gold, Dist, A), LD);
 	.min(LD, op(DistCloser, Closer));
 	DistCloser < 10000;
 	.print("Gold ", gold, " was allocated to ", closer, " options were ", LD);
 	.broadcast(tell, allocated(Gold,Closer)).
 	//-Gold[Source(_)].

-!allocate_miner(Gold)
 <- .print("could not allocate gold ", Gold);
 
/*end of simulation plans*/

@end[atomic]
+end_of_simulation(S,_) : true
 <- .print("-- END ", S, " --");
 	.abolish(init_pos(S,_,_));