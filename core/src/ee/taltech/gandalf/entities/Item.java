package ee.taltech.gandalf.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import ee.taltech.gandalf.components.ItemTypes;
import ee.taltech.gandalf.components.TextureType;
import ee.taltech.gandalf.screens.GameScreen;

public class Item {

    private final ItemTypes type;
    private final Integer id;
    private float xPosition;
    private float yPosition;

    private final Texture texture;
    private float textureWidth;
    private float textureHeight;
    private Texture coinRotationTexture;
    private Animation<TextureRegion> coinRotationAnimation;

    /**
     * Construct Item that is in the inventory and does not need coordinates.
     *
     * @param id item's ID
     * @param type item's type
     */
    public Item(Integer id, ItemTypes type) {
        this.type = type;
        this.id = id;
        this.texture = setTextureBasedOnType();
    }

    /**
     * Construct Item that is on the ground and needs coordinates.
     *
     * @param id item's ID
     * @param type item's type
     * @param xPosition item's x coordinate
     * @param yPosition item's y coordinate
     */
    public Item(Integer id, ItemTypes type, float xPosition, float yPosition) {
        this.type = type;
        this.id = id;
        this.texture = setTextureBasedOnType();
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        if (type == ItemTypes.COIN) {
            coinAnimations();
        }
    }

    /**
     * Get item's type.
     *
     * @return type
     */
    public ItemTypes getType() {
        return type;
    }

    /**
     * Get item's ID.
     *
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Get items texture.
     *
     * @return texture
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * Get texture width.
     *
     * @return float of texture width
     */
    public float getTextureWidth() {
        return textureWidth;
    }

    /**
     * Get texture height.
     *
     * @return float of texture height
     */
    public float getTextureHeight() {
        return textureHeight;
    }

    /**
     * Get item's x coordinate.
     *
     * @return xPosition
     */
    public float getXPosition() {
        return xPosition;
    }

    /**
     * Get item's y coordinate.
     *
     * @return yPosition
     */
    public float getYPosition() {
        return yPosition;
    }

    /**
     * Set item's x coordinate.
     *
     * @param xPosition new x coordinate
     */
    public void setXPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Set item's y coordinate.
     *
     * @param yPosition new y coordinate
     */
    public void setYPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Set items texture based on what type of item it is.
     *
     * @return texture
     */
    private Texture setTextureBasedOnType() {
        Texture itemsTexture = null;
        if (type == ItemTypes.FIREBALL) {
            // Get texture from game screen because then OpenGL does not give error
            itemsTexture = GameScreen.getTexture(TextureType.FIREBALL_BOOK);
            textureHeight = itemsTexture.getHeight() / 2.5f;
            textureWidth = itemsTexture.getWidth() / 2.5f;
        } else if (type == ItemTypes.COIN) {
            itemsTexture = GameScreen.getTexture(TextureType.COIN);
        } else if (type == ItemTypes.HEALING_POTION) {
            itemsTexture = GameScreen.getTexture(TextureType.HEALING_POTION);
            textureHeight = itemsTexture.getHeight();
            textureWidth = itemsTexture.getWidth();
        }
        return itemsTexture;
    }

    /**
     * Create animations for the coin.
     */
    private void coinAnimations() {
        // Define frames in the sprite sheet
        TextureRegion[][] coinRotationFrames2D = TextureRegion.split(texture, 16, 16);

        // Convert 2D array to 1D array
        TextureRegion[] coinRotationFrames = new TextureRegion[10];
        int index = 0;
        for (int j = 0; j < 10; j++) {
            coinRotationFrames[index++] = coinRotationFrames2D[0][j];
        }
        coinRotationAnimation = new Animation<>(0.1F, coinRotationFrames);
    }

    /**
     * Get coin rotation animation.
     */
    public Animation<TextureRegion> getCoinRotationAnimation() {
        return coinRotationAnimation;
    }
}
