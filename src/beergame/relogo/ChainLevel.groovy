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
	static RHO = 0.5
	static ALPHA = 0.5
	static BETA = 1.0
	static THETA = 0.5

	def desiredStock = 12.0
	def currentStock
	def lastOrdersToFulfill
	def lastOrderSent
	def lastShipmentSent
	def expectedDemand
	def ordersToFulfill
	def orderSent
	def shipmentSent
	def orderReceived

	ChainLevel upstreamLevel
	ChainLevel downstreamLevel

	def lastProductPipeline
	def lastOrderPipeline
	def productPipeline = []
	def orderPipeline = [4.0]

	def setup(){
		this.currentStock = 12.0
		this.lastOrdersToFulfill = 0.0
		this.lastOrderSent = 4.0
		this.lastShipmentSent = 4.0
		this.lastProductPipeline = this.productPipeline.clone()
		this.lastOrderPipeline = this.orderPipeline.clone()
		this.expectedDemand = 4.0
		this.orderSent = 0.0
		this.shipmentSent = 0.0
	}

	def receiveShipment(){
		this.productPipeline = this.lastProductPipeline.clone()
		this.productPipeline.add(0, this.upstreamLevel.lastShipmentSent)
		this.currentStock += this.productPipeline.pop()
	}

	def receiveOrder(){
		this.orderPipeline = this.lastOrderPipeline.clone()
		this.orderPipeline.add(0, this.downstreamLevel.lastOrderSent)
		this.orderReceived = this.orderPipeline.pop()
		this.ordersToFulfill = this.lastOrdersToFulfill + this.orderReceived
	}

	def fulfillOrder(){
		if (this.currentStock >= this.ordersToFulfill) {
			this.currentStock -= this.ordersToFulfill
			this.shipmentSent = this.ordersToFulfill
			this.ordersToFulfill = 0.0
		} else {
			this.ordersToFulfill -= this.currentStock
			this.shipmentSent = this.currentStock
			this.currentStock = 0.0
		}
	}

	def makeOrder(){
		def supplyLine = this.productPipeline.sum()
		if (this.upstreamLevel) {
			supplyLine = supplyLine + this.upstreamLevel.lastOrdersToFulfill + this.upstreamLevel.lastOrderPipeline.sum()
		}
		this.expectedDemand = this.THETA * this.orderReceived + (1.0 - this.THETA) * this.expectedDemand
		def desiredSupplyLine = 3 * this.expectedDemand
		def Q = this.desiredStock + this.BETA * desiredSupplyLine
		def totalOrder = this.expectedDemand + this.ALPHA * (Q - this.currentStock - this.BETA * supplyLine)
		this.orderSent =  Math.max(0.0, totalOrder)
	}

	def updateState(){
		this.lastOrderSent = this.orderSent
		this.lastShipmentSent = this.shipmentSent
		this.lastOrdersToFulfill = this.ordersToFulfill
		this.lastProductPipeline = this.productPipeline.clone()
		this.lastOrderPipeline = this.orderPipeline.clone()
	}
}
