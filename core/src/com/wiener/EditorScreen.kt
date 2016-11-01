package com.wiener

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.wiener.entity.EntityFactory
import com.wiener.entity.Mappers
import com.wiener.entity.components.*
import com.wiener.entity.systems.CameraSystem
import com.wiener.entity.systems.PhysicsSystem
import com.wiener.entity.systems.ShapeRenderSystem
import com.wiener.entity.systems.TopDownRenderSystem
import com.wiener.menus.Button
import java.text.SimpleDateFormat
import java.util.*

class EditorScreen() : ScreenAdapter() {
    internal var gameCamera: OrthographicCamera
    internal var guiCamera: OrthographicCamera
    internal var camWidth: Float = 0.toFloat()
    internal var camHeight: Float = 0.toFloat()
    internal var batch = Main.batch
    internal var shapeRenderer = ShapeRenderer()
    internal var font: BitmapFont = Main.assets.get(AssetPaths.NOTO)
    internal var layout = GlyphLayout()
    internal var textInputListener: Input.TextInputListener
    internal var freeCamOn = false
    internal var physicsOn = true
    internal var recordArray = arrayListOf<Vector2>()
    internal var selectingEntity = false
    var currentLevel = 0
    internal var camSpeed = 5f
    internal var selectedEntity: Entity? = null
    internal var originalColor = Color.WHITE
    internal var preview: Entity

    internal val freeCam = Button("Free cam", 1170f, 700f, layout, font)
    internal val resetCam = Button("Reset zoom", 1165f, 670f - layout.height, layout, font)
    internal val undoAll = Button("Undo all", 10f, 550f, layout, font)
    internal val saveFile = Button("Save to file", -10000f, 10f, layout, font)
    internal val loadLine = Button("Load temp as line", 200f, 10f, layout, font)
    internal val loadPolygon = Button("Load temp as polygon", loadLine.bounds.x, loadLine.bounds.y + 20, layout, font)
    internal val reloadLevel = Button("Reload level", loadLine.bounds.x, loadLine.bounds.y + 40, layout, font)
    internal val saveTemp = Button("Save to temp", 10f, 50f, layout, font)
    internal val save = Button("Save", 10f, 10f, layout, font)
    internal val saveEntity = Button("Overwrite to selected entity", saveFile.bounds.x, saveFile.bounds.y + 60, layout, font)
    internal val changeLevel = Button("Change", 1130f, 720f / 2, layout, font)
    internal val editEntity = Button("Edit entity", 10f, 120f, layout, font)
    internal val deleteEntity = Button("Delete entity", editEntity.bounds.x, editEntity.bounds.y + 30, layout, font)
    internal val updatePaths = Button("Update paths", 10f, 620f, layout, font)
    internal val physics = Button("Physics", 1170f, 600f, layout, font)
    internal val back = Button("Back to game", 1145f, 20f, layout, font)
    internal val buttons = arrayListOf(freeCam,
            resetCam,
            undoAll,
            //            saveFile,
            loadLine,
            loadPolygon,
            reloadLevel,
            saveTemp,
            save,
            saveEntity,
            changeLevel,
            editEntity,
            deleteEntity,
            updatePaths,
            physics,
            back)

    init {
        camWidth = Gdx.graphics.width.toFloat()
        camHeight = Gdx.graphics.height.toFloat()
        guiCamera = OrthographicCamera(camWidth, camHeight)
        guiCamera.position.set(camWidth / 2, camHeight / 2, 0f)
        gameCamera = Main.gameScreen.gameCamera

        preview = Entity()
        val transform = TransformComponent(0f, 0f, 0f, 0, 1f)
        preview.add(transform)
        preview.add(ColorComponent(Color.RED))
        Main.engine.addEntity(preview)

        textInputListener = object : Input.TextInputListener {
            override fun input(text: String) {
                currentLevel = text.toInt()
                Main.gameScreen.loadLevel(currentLevel)
            }

            override fun canceled() {
            }
        }
    }

    override fun render(deltaTime: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT)

        draw(deltaTime)
        update()
    }

    fun draw(deltaTime: Float) {
        Main.gameScreen.engine.update(deltaTime)

        guiCamera.update()
        batch.projectionMatrix = guiCamera.combined
        batch.enableBlending()
        batch.begin()


        if (selectingEntity)
            font.draw(batch, "SELECTING ENTITY", camWidth / 2 - 100, camHeight)

        font.color = Color.WHITE
        font.draw(batch, "Current level:\n" + currentLevel, changeLevel.bounds.x, changeLevel.bounds.y + 70)

        font.draw(batch, if (freeCamOn) "ON" else "OFF", freeCam.bounds.x, freeCam.bounds.y - 5)
        font.draw(batch, if (physicsOn) "ON" else "OFF", physics.bounds.x, physics.bounds.y - 5)

        val touch = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        guiCamera.unproject(touch)

        font.color = Color.WHITE
        buttons.forEach { it.draw(batch, touch, font) }

        val unprojectedX = gameCamera.position.x + (touch.x - camWidth / 2) * gameCamera.zoom
        val unprojectedY = gameCamera.position.y + (touch.y - camHeight / 2) * gameCamera.zoom
        font.draw(batch, "$unprojectedX, $unprojectedY", 5f, 300f)
        batch.end()

        // Draw last dot
        shapeRenderer.projectionMatrix = gameCamera.combined
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.ORANGE
        if (recordArray.size > 0) {
            val lastPoint = recordArray.get(recordArray.size - 1)
            shapeRenderer.box(lastPoint.x - 1, lastPoint.y - 1, 0f, 2f, 2f, 0f)
        }
        shapeRenderer.end()
    }

    private fun update() {
        if (Gdx.input.isKeyPressed(Controls.ZOOM_OUT)) {
            gameCamera.zoom += 0.3f
        }
        if (Gdx.input.isKeyPressed(Controls.ZOOM_IN)) {
            gameCamera.zoom -= 0.3f
        }
        if (Gdx.input.isKeyJustPressed(Controls.UNDO)) {
            if (recordArray.size > 1)
                recordArray.removeAt(recordArray.size - 1)
            updatePreview()
        }
        if (Gdx.input.isKeyJustPressed(Controls.SELECT_ENTITY)) {
            recordArray.clear()
            updatePreview()
            selectingEntity = !selectingEntity
        }
        if (Gdx.input.isKeyJustPressed(Controls.DELETE_ENTITY)) {
            deleteEntity()
            recordArray.clear()
            updatePreview()
        }
        if (Gdx.input.isKeyJustPressed(Controls.SET_PLAYER)) {
            val touch = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            guiCamera.unproject(touch)
            setStartPosition(touch.x, touch.y)
        }
        if (Gdx.input.isKeyJustPressed(Controls.SET_OBJECTIVE)) {
            val touch = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            guiCamera.unproject(touch)
            setEndPosition(touch.x, touch.y)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.U)) {
            updatePaths()
        }
        if (freeCamOn) {
            if (Gdx.input.isKeyPressed(Controls.UP)) gameCamera.position.y += camSpeed * gameCamera.zoom
            if (Gdx.input.isKeyPressed(Controls.DOWN)) gameCamera.position.y -= camSpeed * gameCamera.zoom
            if (Gdx.input.isKeyPressed(Controls.LEFT)) gameCamera.position.x -= camSpeed * gameCamera.zoom
            if (Gdx.input.isKeyPressed(Controls.RIGHT)) gameCamera.position.x += camSpeed * gameCamera.zoom
            gameCamera.update()
        }

        if (Gdx.input.justTouched()) {
            val touch = Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
            guiCamera.unproject(touch)

            if (back.bounds.contains(touch.x, touch.y)) {
                Main.game.screen = Main.gameScreen
                Main.engine.getSystem(TopDownRenderSystem::class.java).setProcessing(true)
                Main.engine.getSystem(ShapeRenderSystem::class.java).setProcessing(false)
                Main.engine.getSystem(PhysicsSystem::class.java).setProcessing(true)
                freeCamOn = false
                Main.engine.getSystem(CameraSystem::class.java).setProcessing(true)
                physicsOn = true
                return
            }

            if (selectingEntity) {
                val zoom = gameCamera.zoom
                selectClosestEntity(
                        gameCamera.position.x + (touch.x - camWidth / 2) * zoom,
                        gameCamera.position.y + (touch.y - camHeight / 2) * zoom)
                selectingEntity = false
                return
            }
            if (freeCam.bounds.contains(touch.x, touch.y)) {
                Main.engine.getSystem(CameraSystem::class.java).setProcessing(freeCamOn)
                freeCamOn = !freeCamOn
                return
            }
            if (resetCam.bounds.contains(touch.x, touch.y)) {
                gameCamera.zoom = 1.5f
                return
            }
            if (saveFile.bounds.contains(touch.x, touch.y)) {
                saveFile()
                return
            }
            if (saveTemp.bounds.contains(touch.x, touch.y)) {
                saveTemp()
                return
            }
            if (save.bounds.contains(touch.x, touch.y) || Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                save()
                return
            }
            if (saveEntity.bounds.contains(touch.x, touch.y)) {
                saveEntity()
                return
            }
            if (undoAll.bounds.contains(touch.x, touch.y)) {
                recordArray.clear()
                updatePreview()
                return
            }
            if (reloadLevel.bounds.contains(touch.x, touch.y)) {
                Main.gameScreen.loadLevel(currentLevel)
                return
            }
            if (changeLevel.bounds.contains(touch.x, touch.y)) {
                Gdx.input.getTextInput(textInputListener, "Level number", "", "")
                return
            }
            if (editEntity.bounds.contains(touch.x, touch.y)) {
                recordArray.clear()
                updatePreview()
                selectingEntity = !selectingEntity
                return
            }
            if (deleteEntity.bounds.contains(touch.x, touch.y)) {
                deleteEntity()
                recordArray.clear()
                updatePreview()
                return
            }
            if (updatePaths.bounds.contains(touch.x, touch.y)) {
                updatePaths()
                return
            }
            if (physics.bounds.contains(touch.x, touch.y)) {
                Main.engine.getSystem(PhysicsSystem::class.java).setProcessing(!physicsOn)
                physicsOn = !physicsOn
                return
            }

            // Record points, if no menus buttons were pressed
            val zoom = gameCamera.zoom
            recordArray.add(Vector2(
                    gameCamera.position.x + (touch.x - camWidth / 2) * zoom,
                    gameCamera.position.y + (touch.y - camHeight / 2) * zoom))
            updatePreview()
        }
    }

    private fun saveFile() {
        val dateFormat = SimpleDateFormat("dd.MM.yy HH-mm-ss")
        val date = Date()
        val file = Gdx.files.local("editor_out/" + dateFormat.format(date) + ".txt")
        for (point in recordArray) {
            file.writeString(point.x.toString() + " " + point.y, true)
            file.writeString("\n", true)
        }
    }

    private fun saveTemp() {
        val file = Gdx.files.local(AssetPaths.EDITOR_TEMP)
        file.writeString("", false)
        for (i in 0..recordArray.size - 1) {
            file.writeString("" + recordArray.get(i).x + " " + recordArray.get(i).y, true)
            if (i != recordArray.size - 1) file.writeString("\n", true)
        }
    }

    private fun save() {
        if (recordArray.size < 3) {
            println("Polygons must have at least 3 vertices")
            return
        }

        val dateFormat = SimpleDateFormat("dd.MM.yy HH-mm-ss")
        val date = Date()
        val file = Gdx.files.local(
                AssetPaths.LEVELS_DIR + "/" + currentLevel + "/static/" + dateFormat.format(date) + ".txt")
        file.writeString("", false)
        for (i in 0..recordArray.size - 1) {
            file.writeString("" + recordArray[i].x + " " + recordArray[i].y, true)
            if (i != recordArray.size - 1) file.writeString("\n", true)
        }

        EntityFactory.level(file.toVertices()) // add new entity to engine

        recordArray.clear()
        updatePreview()
        updatePaths()
    }

    private fun setStartPosition(x: Float, y: Float) {
        val unprojectedX = gameCamera.position.x + (x - camWidth / 2) * gameCamera.zoom
        val unprojectedY = gameCamera.position.y + (y - camHeight / 2) * gameCamera.zoom
        val file = Gdx.files.local(
                AssetPaths.LEVELS_DIR + "/" + currentLevel + "/player.txt")
        file.writeString("", false)
        file.writeString("" + unprojectedX + " " + unprojectedY, true)
    }

    private fun setEndPosition(x: Float, y: Float) {
        val unprojectedX = gameCamera.position.x + (x - camWidth / 2) * gameCamera.zoom
        val unprojectedY = gameCamera.position.y + (y - camHeight / 2) * gameCamera.zoom
        val file = Gdx.files.local(
                AssetPaths.LEVELS_DIR + "/" + currentLevel + "/star.txt")
        file.writeString("", false)
        file.writeString("" + unprojectedX + " " + unprojectedY, true)
    }

    private fun saveEntity() {
        if (selectedEntity == null) {
            println("No entity selected")
            return
        }

        if (recordArray.size < 3) {
            println("Terrain must have at least 3 vertices")
            return
        }

        val dir = Gdx.files.local(AssetPaths.LEVELS_DIR + "/" + currentLevel + "/static")
        val files = dir.list()

        val polygonComponent = Mappers.polygon.get(selectedEntity)
        val entityVertices = polygonComponent.polygon
        var file: FileHandle? = null
        for (i in files.indices) {
            val vertices = files[i].toVertices()

            if (Math.round(vertices[0]) == Math.round(entityVertices[0]) && Math.round(vertices[1]) == Math.round(entityVertices[1])) {
                file = files[i]
                break
            }
        }
        if (file == null) {
            println("No terrain file found")
            return
        }

        file.writeString("", false)
        for (i in 0..recordArray.size - 1) {
            file.writeString("" + recordArray.get(i).x + " " + recordArray.get(i).y, true)
            if (i != recordArray.size - 1) file.writeString("\n", true)
        }

        updatePaths()

        selectedEntity = null
    }

    private fun deleteEntity() {
        if (selectedEntity == null) {
            println("No entity selected")
            return
        }

        val dir = Gdx.files.local(AssetPaths.LEVELS_DIR + "/" + currentLevel + "/static")
        val files = dir.list()

        val polygonComponent = Mappers.polygon.get(selectedEntity)
        val entityVertices = polygonComponent.polygon
        var file: FileHandle? = null
        for (i in files.indices) {
            val vertices = files[i].toVertices()

            if (Math.round(vertices[0]) == Math.round(entityVertices[0]) && Math.round(vertices[1]) == Math.round(entityVertices[1])) {
                file = files[i]
                break
            }
        }
        if (file == null) {
            println("No terrain file found")
            return
        }

        file.delete()
        updatePaths()

        Main.engine.removeEntity(selectedEntity) // remove entity
        selectedEntity = null
    }

    // Updates preview, if there's an even amount of vertices
    private fun updatePreview() {
        if (recordArray.size < 2) {
            preview.remove(LineComponent::class.java)
            return
        }

        val array = arrayListOf<Vector2>()
        array.add(recordArray[0])
        for (point in recordArray) {
            array.add(point)
            array.add(point)
        }
        array.add(recordArray[0])
        var size = array.size
        if (size % 2 != 0) size--

        val lines = FloatArray(size * 2)

        for (i in 0..size - 1) {
            lines[i * 2] = array[i].x
            lines[i * 2 + 1] = array[i].y
        }
        preview.remove(LineComponent::class.java)
        preview.add(LineComponent(lines))
    }

    private fun selectClosestEntity(x: Float, y: Float) {
        var distance = 1000000f
        val entities = Main.engine.getEntitiesFor(Family.all(PolygonComponent::class.java, LevelComponent::class.java).get())
        var closestEntity = entities[0]
        for (i in 0..entities.size() - 1) {
            val entity = entities.get(i)
            val pc = Mappers.polygon.get(entity) ?: continue
            val vertices = pc.polygon
            val centroid = vertices.getCentroid()
            val currentDistance = centroid.dst2(x, y)
            if (currentDistance < distance) {
                distance = currentDistance
                closestEntity = entity
            }
        }
        if (selectedEntity != null && Mappers.color.has(selectedEntity)) {
            selectedEntity!!.remove(ColorComponent::class.java)
            selectedEntity!!.add(ColorComponent(originalColor))
        }
        if (Mappers.color.has(closestEntity)) {
            val colorComponent = Mappers.color.get(closestEntity)
            originalColor = colorComponent.color
            closestEntity.remove(ColorComponent::class.java)
            closestEntity.add(ColorComponent(Color.PINK))
        }
        selectedEntity = closestEntity
    }

    private fun updatePaths() {
        val pathFile = Gdx.files.local(AssetPaths.LEVELS_DIR + Main.gameScreen.level + "/paths.txt")
        pathFile.writeString("", false)
        val dir = Gdx.files.local(AssetPaths.LEVELS_DIR + Main.gameScreen.level + "/static")
        if (dir.isDirectory) {
            val files = dir.list()
            files.forEach {
                val fileName = it.toString()
                pathFile.writeString("$fileName\n", true)
            }
        }
    }

    override fun show() {
        currentLevel = Main.gameScreen.level
    }
}