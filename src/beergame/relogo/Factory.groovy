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

@Plural ("Factories")
class Factory extends ChainLevel {
	def setup(){
		this.desiredSupplyLine = 12
		productPipeline = [4.0, 4.0, 4.0]
		this.downstreamLevel = distributors().take(1)
		super.setup()
	}

	def receiveShipment(){
		this.productPipeline = this.lastProductPipeline
		this.productPipeline.add(this.lastOrderSent)
		this.currentStock += this.productPipeline.pop()
	}
}
