import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;

@ScriptManifest(author = "Lexhanatin", name = "AutoKiller", info = "Just an empty script :(", version = 0.1, logo = "")
public final class AutoKiller extends Script  {
	
	Area killingArea = new Area(
		    new int[][]{
		        { 3183, 3289 },
		        { 3183, 3289 },
		        { 3185, 3291 },
		        { 3185, 3294 },
		        { 3186, 3296 },
		        { 3186, 3297 },
		        { 3185, 3298 },
		        { 3185, 3300 },
		        { 3183, 3302 },
		        { 3181, 3303 },
		        { 3179, 3303 },
		        { 3179, 3307 },
		        { 3173, 3307 },
		        { 3173, 3303 },
		        { 3170, 3299 },
		        { 3170, 3295 },
		        { 3169, 3294 },
		        { 3169, 3291 },
		        { 3171, 3289 }
		    }
		);
	
    @Override
    public final int onLoop() throws InterruptedException {
    	if (canKill()) {
    		kill();
    	}
    	else {
    		bank();
    	}
        return random(150, 200);
    }
    
    private void kill() {
    	if (!killingArea.contains(myPosition())) {
    		getWalking().webWalk(killingArea);
    	}
    	else if (isKilling()) {
    		new ConditionalSleep(5000) {
    			@Override
    			public boolean condition() throws InterruptedException {
    				return !isKilling();
    			}
    		}.sleep();
    	}
    	else if (attackMonster()) {
    		new ConditionalSleep(5000) {
    			@Override
    			public boolean condition() throws InterruptedException {
    				return isKilling();
    			}
    		}.sleep();
    	}
    }
    
    private boolean canKill() {
    	//return getInventory().getAmount("Shrimp") > 10;
    	return true;
    }
    
    private boolean isKilling() {
    	return myPlayer().isUnderAttack();
    }
    
    private boolean attackMonster() {
    	NPC monster = getNpcs().closest(npc ->
    		npc.getName().equals("Chicken") &&
    		killingArea.contains(npc) &&
    		!npc.isUnderAttack() &&
    		npc.isAttackable()
    	);
    	
        return monster != null && monster.interact("Attack");
    }
    
    private void bank() throws InterruptedException {
    	if (!Banks.LUMBRIDGE_UPPER.contains(myPosition())) {
    		getWalking().webWalk(Banks.LUMBRIDGE_UPPER);
    	}
    	else if (!getBank().isOpen()) {
    		getBank().open();
    	}
    	else if (!getInventory().isEmpty()) {
    		getBank().depositAll();
    	}
    	else if (getBank().contains("Shrimp")) {
    		getBank().withdrawAll("Shrimp");
    	}
    }
}