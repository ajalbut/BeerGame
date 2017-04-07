package beergame.relogo

import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import repast.simphony.relogo.Stop;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;
import repast.simphony.relogo.schedule.Go;
import repast.simphony.relogo.schedule.Setup;
import beergame.ReLogoObserver;

class UserObserver extends ReLogoObserver{

	@Setup
	def setup(){
		clearAll()

		createFactories(1)
		createDistributors(1)
		createWholesalers(1)
		createRetailers(1)

		ask(chainLevels()){ setup() }
	}

	@Go
	def go(){
		tick()
		ask(chainLevels()){
			receiveShipment()
			receiveOrder()
			fulfillOrder()
			makeOrder()
		}
		ask(chainLevels()){ updateSent() }
	}

	def getFactoryStock(){
		return factories()[0].currentStock
	}

	def getDistributorStock(){
		return distributors()[0].currentStock
	}

	def getWholesalerStock(){
		return wholesalers()[0].currentStock
	}

	def getRetailerStock(){
		return retailers()[0].currentStock
	}
}