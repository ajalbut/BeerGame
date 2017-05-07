package beergame.relogo

import static repast.simphony.relogo.Utility.*
import static repast.simphony.relogo.UtilityG.*

import beergame.ReLogoTurtle
import repast.simphony.relogo.Plural
import repast.simphony.relogo.Stop
import repast.simphony.relogo.Utility
import repast.simphony.relogo.UtilityG
import repast.simphony.relogo.schedule.Go
import repast.simphony.relogo.schedule.Setup

class Retailer extends ChainLevel {
	def setup(){
		productPipeline = [4.0, 4.0]
		this.upstreamLevel = wholesalers()[0]
		super.setup()
	}

	def receiveOrder(){}

	def fulfillOrder(){}

	def makeOrder(){
		if (ticks() >= 4) {
			this.orderSent = 8.0
		} else {
			this.orderSent = 4.0
		}
	}
}
