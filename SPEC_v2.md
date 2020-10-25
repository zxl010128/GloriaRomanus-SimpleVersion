# Final Project Spec v2

After considering the student feedback, we have now simplified the project specification. Please note that there are no new requirements in the revised specification. However, we have removed some features, in order to simplify the tasks. Importantly, whatever work you have done so far will be awarded marks accordingly. In case you have already implemented features that are not part of the Simplified Project Specification, please consult your tutor for trading them against the rest of the work required for the project.

This specification outlines adjustments to the original final project spec, to reduce the workload:

https://gitlab.cse.unsw.edu.au/COMP2511/final_project

**If you have completed functionality which has been removed from this specification, please consult your tutor for trading them against the rest of the work required for the project. After inspecting what you have done, your tutor could propose possible alternatives for you.**

## Summary of changes from original spec

The infrastructure outlined in the original spec will not be required for the basic implementation. These were **ports, mines, markets, farms, troop production buildings, walls (including archery and ballista tower upgrades), smiths, roads**. 

Note here that towers are not required for the basic implementation - which reduces the complexity of the battle resolver. Additionally, roads not being required simplifies soldier movement.

The **campaign AI** outlined in the original spec is not required for the basic implementation, and **offline multiplayer** will become part of the basic specification (note online multiplayer is still an extension and is not required for the basic implementation). For offline multiplayer with no campaign AI, you should allocate all provinces between the human-players when the game starts with some reasonable mechanism (such as randomly allocating the provinces). This should make implementing the basic project easier.

**Mercenaries** are no longer required in the basic specification.

**If you have completed functionality which has been removed from this specification, please consult your tutor for trading them against the rest of the work required for the project. After inspecting what you have done, your tutor could propose possible alternatives for you.**

# Simplified Basic Project Specification

This section re-states the basic project completeness requirements with the above adjustments, for clarity.

## Requirements for grading levels

The following are requirements for different grades for completeness in milestones 2 and 3 (for milestone 2, the appropriate functionality must be modelled in the backend classes):

* Pass (>=50%)
    * Player must be able to invade (attempting to conquer) enemy provinces
    * Player must be able to move troops between adjacent regions 1 turn at a time (do not need to implement movement of troops across multiple provinces for a pass)
    * Player must be able to recruit soldiers of any category (not necessary to consider money in pass-level, but it should take the appropriate number of turns)
    * Trivial but sensible implementation of battle resolver. For example (you may implement another sensible implementation, but you must be able to explain why it is suitable during your iteration demos):
        * Battle resolver: army strength calculated as the sum of *number of soldiers in unit x attack x defense* for all units in the army. Each army then has a uniformly random chance of winning calculated as: *army strength/(army strength + enemy army strength)*. The winning army eliminates a uniformly random proportion of the losing army from:
        *((winning army strength)/(winning army strength+losing army strength) x 100%)* to 100% of the losing army.
        The losing army eliminates a uniformly random proportion of the winning army from:
        0% to *((losing army strength)/(winning army strength+losing army strength) x 100%)* to 100% of the winning army.
        A successful invading army should destroy the enemy army as per the spec, and move its soldiers into the captured province.
    * Offline multiplayer implemented - ending the turn should transfer control to the next faction of a human player

* Credit (>=65%)
    * All requirements for a pass, and all of the following:
    * Player is able to move troops between multiple provinces at a time. All rules regarding movement of troops implemented (e.g. not being able to move units moved into a province conquered in the current turn)
    * Costs for soldiers are implemented, and money (gold) is implemented
    * Wealth and taxes mechanic is implemented
    * Can win the game by conquering all provinces, or lose the game by losing all provinces, and see an appropriate victory/defeat message

* Distinction (>=75%)
    * All requirements for a credit, and all of the following:
    * Campaign victory and loss implemented fully, including conjunction/disjunctions of subgoals, and random selection of a campaign goal when starting a campaign
    * Implement main menu, including ability to choose factions from the main menu

* High Distinction (>=85%)
    * All requirements for a distinction, and all of the following:
    * Soldier special abilities implemented
    * Strong progress towards full battle resolver implementation

* Full marks
    * All requirements for a high distinction, and all of the following:
    * Full implementation of battle resolver (including all aspects within these such as inflicting of casualties, routing, etc...)
    * All functionality in requirements implemented correctly

If you exceed the requirements of a grading level in regards to some mechanics, but fall short on other mechanics, the marker will make a judgement regarding the degree to which the functionality is completed and assign a mark appropriate to this (i.e. doing well in some functionality can compensate for poorer implementation in other functionality).

Note that functionality already implemented in the starter code will not receive marks for completion, however marks may be deducted if end-user functionality already in the starter code does not work in milestone 3 (note this does not apply to milestone 2, because the only backend class is a *Unit* class).

Marks may be subtracted from completeness where the appropriate rules were not adhered to from the spec, even when not directly specified in the above requirements, where applicable to the requirements for that grade level (e.g. failing to implement the rounding of calculations for implementations of any grade, or failing to implement that only the Romans can build highways for a Distinction grade).

Milestone 2 requires that the relevant functionality is implemented in the backend classes. It is important that all logic regarding the game mechanics is contained inside these backend classes. Developing the frontend in milestone 3 should only involve "adding a UI" on top of the backend implementation (failure to design around this from milestone 2 implies a failure to decouple the View and Model... you should be preparing to adhere to Model-View-Controller architecture). Thus, failure to model functionality in the milestone 2 backend (model) classes, and implementing it instead in view/controller code, may result in a loss of marks.

## Preliminary client requirements

The client desires a grand-strategy game, set in the time of Ancient Rome (around 200 BC), where the player can play as a faction of the time (such as the Romans, Carthaginians, Gauls, Celtic Britons, Spanish, Numidians, Egyptians, Seleucid Empire, Pontus, Amenians, Parthians, Germanics, Greek City States, Macedonians, Thracians, Dacians), with the overall goal of conquering all provinces in the game map (or succeeding at another grand victory objective).

The game should be a turn-based military/economic strategy game - each faction engages in actions during sequential "turns", where they perform all actions to manage their armies and provinces for a single year in their turn, before clicking "End turn", which will result in all other factions performing their turns sequentially.

The basic unit of currency in the game will be "Gold". Each unit of troops has a price which must be paid in gold upfront, without going into debt (unless implementing the entirety of the lending and bankruptcy mechanic extensions).

### Movement of Soldiers

Factions should be able to move troops between regions and attack neighbouring enemy regions. The type of unit will affect the number of regions through which troops can move in a single turn.

Factions should be able to attack multiple enemy provinces in a single turn, with the following restrictions:
* To attack multiple enemy provinces in a single turn they must use different instances of units
* Factions cannot "attack through" a settlement they conquered in the current turn (i.e. if they conquered settlement A in the current turn, they cannot move troops to settlement A and use them to attack settlement B directly from settlement A)


If troops have been used to invade a province, or are moved into a province invaded in the current turn, they cannot be moved for the rest of the turn.

Movement points are expended for every province passed through from the starting province, to the destination province. The starting province is included in this calculation, whilst the destination province is not included.

Units cannot move through enemy territory. Units should automatically move by the shortest path between non-adjacent provinces (shortest path is defined as the path which will expend the fewest movement points).

If the player attempts to move units to a province further than the movement points allow the units to be moved in the turn, or is unable to move the units from the starting province to the destination province, the attempt to move the units should be rejected.

By default, the number of movement points available to a unit each turn depends on its category:
* Cavalry = 15 movement points
* Infantry = 10 movement points
* Artillery = 4 movement points

For the basic specification, moving across a province should use 4 movement points.

The player should be able to select several units for movement from a province to another province. When multiple units are moved at the same time, the maximum distance they can be moved during the turn together should be limited by the number of movement points of the unit with the fewest movement points. Troops should lose movement points at the unit's normal rate regardless of whether they were moved with other units.

For example, if a unit of infantry with 10 movement points was moved with a unit of artillery with 4 movement points, and moving across 1 province used 4 movements points for both units, the artillery would have used all of its movement points and be unable to move for the duration of the turn. However, the infantry unit would be able to be selected independently and moved further with its remaining 6 movement points.

### Soldiers

All soldiers have a base-level initial training cost (which must be immediately paid by the player). If soldiers are killed in battle, or disbanded, the affected soldiers are removed from the game, without refund to the player. Soldiers do not incur upkeep costs.

For the basic game, any type of soldiers available to a players faction should be recruitable in any province owned by this player.

Soldiers who have finished training appear at the *beginning* of the turn they are due to finish training. For example, if the user selects to recruit a unit of soldiers taking 1 turn to train at turn 2, the soldiers will be available at the start of turn 3.

If a province is conquered whilst troops are being trained, the troop production is cancelled and no refund is made to the player.

Each province can work on training 2 units per turn. Some units (e.g. elephants) take 2 or more turns to produce (these units must occupy a training slot for the required number of turns - having spare training slots doesn't allow the training to be sped up).

### Soldier Special Abilities and Combat Statistics

The following special abilities should be available and implemented automatically.

* For all Roman legionary units: "Legionary eagle" - provides +1 morale to all friendly units in the province. For every legionary eagle lost to the enemy (by the unit being destroyed defending a province) all friendly units across all provinces suffer a 0.2 penalty to morale until the settlement is recaptured (down to a minimum of 1 morale)
* For all Gallic/Celtic Briton/Germanic berserker units: "Berserker rage" - unit receives infinite morale and double melee attack damage, but has no armor or shield protection, in all battles
* For all melee cavalry: "Heroic charge" - where the army has fewer than half the number of units as the enemy, this cavalry unit will double its charge attack damage, and have 50% higher morale
* For all pikemen or hoplite units: "Phalanx" - these hoplites or pikemen have double the melee defence, but half of the speed, as they are otherwise configured to have
* For all javelin-skirmisher units: "skirmisher anti-armour" - in ranged engagements, troops fighting these skirmishers only receive half the protection from armour they would receive otherwise
* For all elephant units: "Elephants running amok" - during any engagement with elephants, there is a 10% chance that the casualties inflicted by a unit of elephants will instead be directed at a random allied unit (as if the elephants were battling the allied unit directly)
* For all horse-archer units: "Cantabrian circle" - when enemy missile units engage this unit of horse archers, the enemy missile units will suffer a 50% loss to missile attack damage
* For all druid units: "Druidic fervour" - allied units in an army with druids enjoy a 10% bonus to morale, and enemy units suffer a 5% penalty to morale, whilst the druids haven't routed. The effect of this ability is amplified by scalar addition, and can be amplified up to 5 times (e.g. 2 druids results in allies receiving 20% bonus to morale and enemy units suffering 10% penalty, however 6 druids provides 50% bonus and 25% penalty respectively)
* For all melee infantry: "Shield charge" - for every 4th engagement by this unit of melee infantry per battle, the value of shield defense is added to this unit's attack damage value

Where both scalar addition bonuses (e.g. +1 attack damage) and multiplicative bonuses (e.g. 20% loss in speed) apply due to filling multiple categories (e.g. druids are both melee infantry and druids), the scalar addition bonuses are applied first.

### Wealth and Taxes

Province wealth represents the collective wealth of the people in the province. It is distinct from the treasury; treasury is the gold you spend on troops. Province wealth is composed of town wealth (from the accumulation of turn-by-turn wealth generation).

Wealth cannot be spent; it is taxed to produce tax revenue for the faction's treasury. The tax revenue generated is always equal to *province wealth x province tax rate*

The minimum wealth of a province is 0 - for a province with 0 town wealth.

Taxes are managed at the province level by the player.

Higher tax rates reduce the growth of town-wealth in a region (and can make it negative).

The effects of different tax levels are:

* Low tax = +10 town-wealth growth per turn for the province, tax rate = 10%
* Normal tax = No effect on per turn town-wealth growth, tax rate = 15%
* High tax = -10 town-wealth growth per turn for the province (i.e. 10 gold loss to wealth per turn), tax rate = 20%
* Very high tax = -30 town-wealth growth per turn for the province, tax rate = 25%, -1 morale for all soldiers residing in the province

### Campaign Victory

Basic goals in the game should include:

* Conquering all territories (CONQUEST goal)
* Accumulating a treasury balance of 100,000 gold (TREASURY goal)
* Accumulating faction wealth of 400,000 gold (WEALTH goal)

When starting a campaign game, a logical conjunction(AND)/disjunction(OR) of basic goals, or a conjunction/disjunction of other conjunctions/disjunctions should be chosen randomly (a uniformly random choice from all possible goal expressions where each basic goal exists in the expression at most once). For example:

{ "goal": "AND", "subgoals":
  [ { "goal": "TREASURY" },
    { "goal": "OR", "subgoals":
      [ {"goal": "CONQUEST" },
        {"goal": "WEALTH" }
      ]
    }
  ]
}

This example of a victory condition would allow victory in either of the following scenarios:

* TREASURY and CONQUEST goals achieved
* TREASURY and WEALTH goals achieved

Upon reaching the victory condition, the game should be automatically saved, and the user presented with an interface congratulating them on their victory. Re-loading the automatic save should allow the user to continue playing the game without further victory prompts.

### Basic Campaign Game Interactions

Upon starting/loading a game, the user should be presented with a game map and a menu/menus to perform actions. The game map should display a map of the world, split into historically accurate, clickable provinces (as in the starter-code). Each province should be allocated to a faction, and display the flag of the faction, the province wealth, the number of soldiers, and the faction name.

The user should be able to view the current game year and turn on the screen whilst playing the game. The user should be able to select to finish the turn by clicking an "End turn" button.

The user should be able to save their campaign game at any time (so that they can load it and resume gameplay later).

### Basic Battle Resolver

Battles are composed of a sequence of "skirmishes" between a single unit from each of the armies, which are run until an army is eliminated or routed entirely. The army left standing is victorious. If the the attacking army in an invasion is victorious, the province is conquered by the invaders.

At the beginning of a skirmish, a unit is randomly chosen from each of the armies (uniformly random).

During a skirmish, both units engage in a sequence of `engagements` against each other until a unit **successfully** routes (runs away from the battle permanently) or is defeated.

If both units are melee units, there is a 100% chance of a melee engagement. If both units are missile units, there is 100% chance of a missile engagement.

If a battle lasts longer than 200 engagements, the outcome should be a draw. The invading army in a draw should return to the province it invaded from.

**The result of all engagements should be presented to the user via visual or text information.** A JavaFX `TextArea` is included in the starter-code UI to make this easier.

#### Chance of ranged/melee engagement where there is a ranged and melee unit in the engagement

If there is 1 ranged unit (artillery, horse archers, missile infantry) and 1 melee unit, there is a base-level 50% chance for the engagement to be a ranged engagement, and a base-level 50% chance for an engagement to be a melee engagement.

The base-level chance of engagement to be a melee engagement (where the engagement has both a melee and missile unit) is increased by *10% x (speed of melee unit - speed of missile unit)* (value of this formula can be negative)

Where the engagement has both a melee and missile unit, the maximum chance for an engagement to be either a ranged or melee engagement is 95% in either case.

#### Inflicting of casualties in an engagement

Melee units cannot inflict damage in a ranged engagement.

Ranged units in a ranged engagement inflict casualties against the opposing unit equal to (up to a maximum of the entire enemy unit, with a minimum of none of the enemy unit):

* (size of enemy unit at start of engagement x 10%) x (Missile attack damage of unit/(effective armor of enemy unit + effective shield of enemy unit)) x (N+1)

Where *N* is a normally distributed random variable with mean 0 and standard deviation 1 (standard normal distribution).

**HINT: check Random.nextGaussian() to obtain a random value from a random normal distribution:**

https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Random.html#nextGaussian()

You should ensure the ranged attack damage above incorporates the effect of any bonuses/penalties (e.g. the 10% loss of missile attack damage from fire arrows).


Melee cavalry/chariots/elephants will have an attack damage value in all engagements equal to their *melee attack damage + charge value*. Infantry and artillery do not receive a charge statistic (only cavalry/chariots/elephants do).

Units in a melee engagement inflict casualties against the opposing unit equal to (up to a maximum of the entire enemy unit, with a minimum of none of the enemy unit):

* (size of enemy unit at start of engagement x 10%) x (Effective melee attack damage of unit/(effective armor of enemy unit + effective shield of enemy unit + effective defense skill of enemy unit)) x (N+1)

Where *N* is a normally distributed random variable with mean 0 and standard deviation 1 (standard normal distribution).

Note that all effective attributes in the formula should incorporate the effect of any bonuses/penalties (such as formations such as phalanx formation, charge bonuses where applicable for cavalry/chariots/elephants).

Note that a Gaussian distribution can return negative values, therefore it is important that you apply the specified bounds to the minimum and maximum values of these formulas (maximum casualties is entire enemy unit, minimum casualties is none of the enemy unit). This is to prevent damage causing "healing", or the appearance of units with a negative number of troops.

#### Breaking a unit

Following the inflicting of casualties during each engagement, random chance may result in a unit "breaking" and attempting to flee the battle.

The base-level probability of a unit "breaking" following an engagement is calculated as:

*100% - (morale x 10%)*

For example, a unit of peasants with 3 morale would have a base chance of breaking after each engagement of:
100% - (3 x 10%) = 100% - 30% = 70%

Note that the morale value in the above formula should be morale after applying all campaign/battle morale adjustments (such as morale loss due to very high tax in the province, morale loss due to scary units in the enemy army).

The chance of breaking is increased by (a scalar addition):

*(casualties suffered by the unit during the engagement/number of troops in the unit at the start of the engagement)/(casualties suffered by the opposing unit during the engagement/number of troops in the opposing unit at the start of the engagement) x 10%*

For example, if our above unit of peasants suffered casualties equal to half of the units size at the start of the engagement, and the opposing unit suffered casualties equal to a quarter of its size at the start of the engagement, then the chance of breaking is increased by:
(1/2) / (1/4) x 10% = 2 x 10% = 20%
And thus the unit of peasants would have a 70% + 20% = 90% chance of breaking.

i.e. if your unit loses a larger proportion of it's soldiers than the opposing unit in an engagement, the chance of your unit to break is increased by a larger value than the enemy unit from the base fleeing chance.

However, for any engagement, the minimum chance of breaking is 5%, and the maximum chance of breaking is 100%, after these calculations/adjustments.

If both units break, they both successfully flee the battle without inflicting further casualties upon each other.

#### Routing

If a unit "breaks" (and the opposing unit does not), it will repeatedly attempt to flee the battle (each failure resulting in an engagement in which the fleeing unit suffers casualties, without damaging the pursuing unit), until it is successful in doing so or is destroyed (units which break will not attempt to return to the battle).

If a unit breaks, there is a base-level 50% chance of it successfully routing for every attempt. The minimum chance a unit can have to successfully route is 10%, and the maximum chance it can have is 100%. The speed of the units in the engagement affect the chance of successfully routing, according to the following formula (but is superseded by the 10%/100% rule outlined above):

*chance of routing successfully = 50% + 10% x (speed of routing unit - speed of pursuing unit)*

For example, if the speed of the routing unit is 8, and the speed of the pursuing unit is 5, the chance of routing successfully in a single engagement is 80%. If the speed of the routing unit is 1, and the speed of the pursuing unit is 7, the chance of routing successfully in a single engagement is 10% (rather than -10%, since the minimum chance for a unit to escape is 10% per attempt).

Note that if a unit successfully routes from the battle, if it is in an attacking army which loses the battle, it will return to the province from which it attacked, but if the attacking army wins the battle, the routing unit will join the army in moving into the new province. The entirety of a defending army which is defeated/routed is destroyed.

### Main Menu

Upon starting the game, the user should be presented with a main menu.

From here, the user should be able to choose to start a "Campaign Game". The user should be able to select an option for a "Battle Resolver", which will apply throughout the duration of the campaign. The user should at least be able to select the "Basic Battle Resolver", details of which have been outlined above. If you develop different battle resolvers, the user should be able to select their preferred option. If you have implemented campaign AI, the user should be able to select the preferred campaign AI. 

The user should be able to select all factions for which a human-player will play the game (for offline multiplayer).

After selecting any/all relevant settings, the user should be able to start the campaign game.

The user should be able to load a previously saved game from the main menu (or a follow-on-menu).


### Miscellaneous Points

All calculations, unless otherwise specified, should be rounded to the nearest integer in the result (e.g. if a market reduces the price of a building costing 70 gold pieces by 3% (for the market building in the original spec), the price of the building will be 68 gold pieces, rather than 67.9). However, the minimum training cost for troops, and minimum purchase price for a building, is 1 gold piece.

Each turn takes 1 year. Thus, in this spec, the terms "year" and "turn" are synonymous.

Bankruptcy is not possible in the game outlined in the basic specification because there are no recurring costs, and players can only spend money in the treasury (they cannot raise debt). Implementing an extension for upkeep would also require you to consider how bankruptcy would be handled.

When a faction loses all of its provinces, it should be removed from the game. If a player loses all of their provinces, the game should present a "Game lost" screen, and after clicking "Next" the players turn should be ended and control transferred to the next human players faction (or moved to the main menu if no human players left).
