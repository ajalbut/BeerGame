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

class ChainLevel extends ReLogoTurtle {
	static float RHO = 0.5
	static float ALPHA = 0.5
	static float BETA = 1.0
	static float THETA = 0.5

	float desiredStock = 12
	float desiredSupplyLine = 12
	float Q
	float currentStock
	float lastOrdersToFulfill
	float lastOrderSent
	float lastShipmentSent
	float expectedDemand
	float ordersToFulfill
	float orderSent
	float shipmentSent

	def upstreamLevel
	def downstreamLevel

	def lastProductPipeline
	def lastOrderPipeline
	def productPipeline = []
	def orderPipeline = [4.0]

	def setup(){
		this.Q = this.desiredStock + this.BETA * this.desiredSupplyLine
		this.currentStock = 12.0
		this.lastOrdersToFulfill = 0.0
		this.lastOrderSent = 4.0
		this.lastShipmentSent = 4.0
		this.lastProductPipeline = this.productPipeline
		this.lastOrderPipeline = this.orderPipeline
		this.expectedDemand = 4.0
		this.orderSent = 0.0
		this.shipmentSent = 0.0
	}

	def receiveShipment(){
		this.productPipeline = this.lastProductPipeline
		this.productPipeline.add(this.upstreamLevel.lastShipmentSent)
		this.currentStock += this.productPipeline.pop()
	}

	def receiveOrder(){
		this.orderPipeline = this.lastOrderPipeline
		this.orderPipeline.add(this.downstreamLevel.lastOrderSent)
		this.ordersToFulfill = this.lastOrdersToFulfill + this.orderPipeline.pop()
	}

	def fulfillOrder(){
		if (this.currentStock >= this.ordersToFulfill) {
			this.currentStock -= this.ordersToFulfill
			this.shipmentSent = this.ordersToFulfill
			this.ordersToFulfill = 0
		} else {
			this.ordersToFulfill -= this.currentStock
			this.shipmentSent = this.currentStock
			this.currentStock = 0
		}
	}

	def makeOrder(){
		float supplyLine = this.productPipeline.sum()
		if (this.upstreamLevel) {
			supplyLine = supplyLine + this.upstreamLevel.lastOrdersToFulfill + this.upstreamLevel.lastOrderPipeline.sum()
		}
		this.expectedDemand = this.THETA * this.lastShipmentSent + (1 - this.THETA) * this.expectedDemand
		float totalOrder = this.expectedDemand + this.ALPHA * (this.Q - this.currentStock - this.BETA * supplyLine)
		this.orderSent =  Math.max(0, totalOrder)
	}

	def updateState(){
		this.lastOrderSent = this.orderSent
		this.lastShipmentSent = this.shipmentSent
		this.lastOrdersToFulfill = this.ordersToFulfill
		this.lastProductPipeline = this.productPipeline
		this.lastOrderPipeline = this.orderPipeline
	}
}
