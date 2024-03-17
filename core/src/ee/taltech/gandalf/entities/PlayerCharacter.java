package ee.taltech.gandalf.entities;

import com.badlogic.gdx.physics.box2d.*;
import ee.taltech.gandalf.network.messages.game.KeyPress;
import ee.taltech.gandalf.network.messages.game.MouseClicks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerCharacter {

    public static final Integer WIDTH = 55;
    public static final Integer HEIGHT = 70;

    private Body body;
    public int xPosition;
    public int yPosition;
    public int playerID;
    public boolean moveLeft;
    boolean moveRight;
    boolean moveDown;
    boolean moveUp;
    public double mouseXPosition;
    public double mouseYPosition;
    public boolean mouseLeftClick;
    public Integer health;
    public double mana;
    private List<Item> inventory;
    private Integer selectedSlot;

    /**
     * Construct PlayerCharacter.
     *
     * @param playerID player's ID
     */
    public PlayerCharacter(Integer playerID) {
        // Here should be the random spawn points for a PlayerCharacter
        this.xPosition = 0;
        this.yPosition = 0;
        this.playerID = playerID;
        health = 100;
        mana = 100;

        inventory = new ArrayList<>();
        // Add empty items to inventory slots
        inventory.add(0, null);
        inventory.add(1, null);
        inventory.add(2, null);

        selectedSlot = 0; // By default, player's first inventory slot is selected
    }

    /**
     * Create player's hit box.
     *
     * @param world world, where hit boxes are in
     */
    public void createHitBox(World world) {
        // Create a dynamic or static body for the player
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(xPosition, yPosition);
        Body hitBoxBody = world.createBody(bodyDef);

        // Create a fixture defining the hit box shape
        PolygonShape hitBoxShape = new PolygonShape();
        hitBoxShape.setAsBox(PlayerCharacter.WIDTH, PlayerCharacter.HEIGHT);

        // Attach the fixture to the body
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = hitBoxShape;
        fixtureDef.density = 1.0f;
        hitBoxBody.createFixture(fixtureDef);

        // Clean up
        hitBoxShape.dispose();

        hitBoxBody.setUserData(this);
        this.body = hitBoxBody;
    }

    /**
     * Set player's health.
     *
     * @param newHealth new health that is set to player
     */
    public void setHealth(Integer newHealth) {
        health = newHealth;
    }

    /**
     * Set player's mana.
     *
     * @param newMana new mana that is set to player.
     */
    public void setMana(double newMana) {
        mana = newMana;
    }

    /**
     * Get player's body.
     *
     * @return body
     */
    public Body getBody() {
        return body;
    }

    /**
     * Get player's inventory.
     *
     * @return inventory
     */
    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Get player's selected slot.
     *
      * @return selectedSlot
     */
    public Integer getSelectedSlot() {
        return selectedSlot;
    }

    /**
     * Set player's selected slot.
     *
     * @param selectedSlot new selected slot
     */
    public void setSelectedSlot(Integer selectedSlot) {
        this.selectedSlot = selectedSlot;
    }

    /**
     * Pick up item from the ground.
     *
     * @param item picked up item
     */
    public void pickUpItem(Item item) {
        if (inventory.get(selectedSlot) == null) { // Pick up item when selected slot is empty
            inventory.remove((int) selectedSlot); // Remove null from the spot
            inventory.add(selectedSlot, item); // Add item to the spot
        }
    }

    /**
     * Drop item.
     *
     * @param droppedItemsId ID of the item that is dropped
     * @return droppedItem
     */
    public Item dropItem(Integer droppedItemsId) {
        for (Item item : inventory) {
            if (!Objects.equals(item, null) && Objects.equals(item.getId(), droppedItemsId)) { // Find correct item
                int itemsIndex = inventory.indexOf(item); // Get items index
                inventory.remove(item); // Remove item from inventory
                inventory.add(itemsIndex, null); // Put null in the empty spot
                return item;
            }
        }
        return null; // Should never get to this point
    }

    /**
     * Update player's position.
     */
    public void updatePosition() {
        // updatePosition is activated every TPS.

        // One key press distance that a character travels.
        int distance = 8;
        // Diagonal movement correction formula.
        int diagonal = (int) (distance / Math.sqrt(2));
        if (moveLeft && moveUp) {
            this.xPosition -= diagonal;
            this.yPosition += diagonal;
        } else if (moveLeft && moveDown) {
            this.xPosition -= diagonal;
            this.yPosition -= diagonal;
        } else if (moveRight && moveUp) {
            this.xPosition += diagonal;
            this.yPosition += diagonal;
        } else if (moveRight && moveDown) {
            this.xPosition += diagonal;
            this.yPosition -= diagonal;
        } else {
            oneDirectionMovement(distance);
        }
        // Set the position of the Box2D body to match the player's coordinates
        body.setTransform( (float) xPosition + 91, (float) yPosition + 70, body.getAngle());
    }

    /**
     * Moving only in one direction.
     *
     * @param distance how much player is moving
     */
    private void oneDirectionMovement(int distance) {
        if (moveLeft) {
            this.xPosition -= distance;
        }
        if (moveRight) {
            this.xPosition += distance;
        }
        if (moveUp) {
            this.yPosition += distance;
        }
        if (moveDown) {
            this.yPosition -= distance;
        }
    }

    /**
     * Set move hover values.
     *
     * @param mouseClicks where move was clicked
     */
    public void setMouseHover(MouseClicks mouseClicks) {
        this.mouseXPosition = mouseClicks.mouseXPosition;
        this.mouseYPosition = mouseClicks.mouseYPosition;
        this.mouseLeftClick = mouseClicks.leftMouse;
    }

    /**
     * Set player's movement based on keypress.
     *
     * @param keyPress incoming keypress
     */
    public void setMovement(KeyPress keyPress) {
        // Set a action where player should be headed.
        if (keyPress.action == KeyPress.Action.LEFT) {
            this.moveLeft = keyPress.pressed;
        }
        if (keyPress.action == KeyPress.Action.RIGHT) {
            this.moveRight = keyPress.pressed;
        }
        if (keyPress.action == KeyPress.Action.UP) {
            this.moveUp = keyPress.pressed;
        }
        if (keyPress.action == KeyPress.Action.DOWN) {
            this.moveDown = keyPress.pressed;
        }
    }

    /**
     * Set player characters position.
     *
     * @param xPosition x coordinate
     * @param yPosition y coordinate
     */
    public void setPosition(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }
}
