package ru.ino.senseix;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import java.io.File;

public class VideoLayout extends FrameLayout implements TextureView.SurfaceTextureListener, 
MediaPlayer.OnPreparedListener,
MediaPlayer.OnCompletionListener {
	private boolean autoPlay = true;
	private boolean looping = false;
	private boolean muted = false;
	private LinearLayout overlayLayout;
	private int pendingSeekMs = -1;
	private String pendingSource;
	private MediaPlayer player;
	private boolean prepared = false;
	private MediaPlayer.OnPreparedListener preparedListener;
	private MediaPlayer.OnCompletionListener completionListener;
	private Surface surface;
	private boolean surfaceReady = false;
	private TextureView videoView;
	
	public VideoLayout(Context context) {
		super(context);
		this.init(context, null);
	}
	
	public VideoLayout(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.init(context, attributeSet);
	}
	
	public VideoLayout(Context context, AttributeSet attributeSet, int n) {
		super(context, attributeSet, n);
		this.init(context, attributeSet);
	}
	
	private void applyMute() {
		MediaPlayer mediaPlayer = this.player;
		if (mediaPlayer != null) {
			float f = this.muted ? 0.0f : 1.0f;
			mediaPlayer.setVolume(f, f);
		}
	}
	
	private void createPlayer() {
		this.release();
		this.player = new MediaPlayer();
		this.player.setLooping(this.looping);
		this.player.setOnPreparedListener(this);
		this.player.setOnCompletionListener(this);
	}
	
	private void init(Context context, AttributeSet attrs) {
		int gravity = 0x800033;
		int orientation = 1;
		int padding = 0;
		
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{16842927, 16842948, 16842965});
			gravity = typedArray.getInt(0, 0x800033);
			orientation = typedArray.getInt(1, 1);
			padding = typedArray.getDimensionPixelSize(2, 0);
			typedArray.recycle();
		}
		
		TextureView textureView = new TextureView(context);
		this.videoView = textureView;
		textureView.setSurfaceTextureListener(this);
		this.videoView.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
		
		LinearLayout linearLayout = new LinearLayout(context);
		this.overlayLayout = linearLayout;
		linearLayout.setOrientation(orientation);
		this.overlayLayout.setGravity(gravity);
		this.overlayLayout.setPadding(padding, padding, padding, padding);
		this.overlayLayout.setLayoutParams((ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-1, -1));
		
		super.addView((View)this.videoView);
		super.addView((View)this.overlayLayout);
		this.createPlayer();
	}
	
	public void addView(View view) {
		LinearLayout linearLayout = this.overlayLayout;
		if (linearLayout != null && view != this.videoView && view != linearLayout) {
			linearLayout.addView(view);
			return;
		}
		super.addView(view);
	}
	
	public void addView(View view, ViewGroup.LayoutParams layoutParams) {
		LinearLayout linearLayout = this.overlayLayout;
		if (linearLayout != null && view != this.videoView && view != linearLayout) {
			if (layoutParams instanceof LinearLayout.LayoutParams) {
				layoutParams = new LinearLayout.LayoutParams((LinearLayout.LayoutParams)layoutParams);
			} else if (layoutParams instanceof FrameLayout.LayoutParams) {
				FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams)layoutParams;
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(flp.width, flp.height);
				llp.gravity = flp.gravity;
				llp.leftMargin = flp.leftMargin;
				llp.topMargin = flp.topMargin;
				llp.rightMargin = flp.rightMargin;
				llp.bottomMargin = flp.bottomMargin;
				layoutParams = llp;
			} else {
				layoutParams = new LinearLayout.LayoutParams(layoutParams);
			}
			this.overlayLayout.addView(view, layoutParams);
			return;
		}
		super.addView(view, layoutParams);
	}
	
	protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
		return layoutParams instanceof FrameLayout.LayoutParams;
	}
	
	protected FrameLayout.LayoutParams generateDefaultLayoutParams() {
		return new FrameLayout.LayoutParams(-1, -1);
	}
	
	public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
		return new FrameLayout.LayoutParams(this.getContext(), attributeSet);
	}
	
	public int getCurrentPositionMs() {
		MediaPlayer mediaPlayer = this.player;
		if (mediaPlayer != null && this.prepared) {
			return mediaPlayer.getCurrentPosition();
		}
		return 0;
	}
	
	public int getDurationMs() {
		MediaPlayer mediaPlayer = this.player;
		if (mediaPlayer != null && this.prepared) {
			return mediaPlayer.getDuration();
		}
		return 0;
	}
	
	public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int n, int n2) {
		this.surface = new Surface(surfaceTexture);
		this.surfaceReady = true;
		MediaPlayer mediaPlayer = this.player;
		if (mediaPlayer != null) {
			mediaPlayer.setSurface(this.surface);
			if (this.prepared && this.autoPlay) {
				this.player.start();
			}
		}
	}
	
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
		this.surfaceReady = false;
		if (this.surface != null) {
			this.surface.release();
		}
		this.surface = null;
		return true;
	}
	
	public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int n, int n2) {
	}
	
	public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
	}
	
	public void pause() {
		MediaPlayer mediaPlayer = this.player;
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			this.player.pause();
		}
	}
	
	public void play() {
		MediaPlayer mediaPlayer = this.player;
		if (mediaPlayer != null && this.prepared) {
			mediaPlayer.start();
		}
	}
	
	public void release() {
		MediaPlayer mediaPlayer = this.player;
		if (mediaPlayer != null) {
			try {
				mediaPlayer.stop();
			} catch (Exception exception) {
				// Ignore
			}
			try {
				this.player.release();
			} catch (Exception exception) {
				// Ignore
			}
		}
		this.player = null;
		this.prepared = false;
	}
	
	public void resume() {
		this.play();
	}
	
	public void seekTo(int n) {
		MediaPlayer mediaPlayer = this.player;
		if (mediaPlayer == null) {
			return;
		}
		if (this.prepared && this.surfaceReady) {
			try {
				mediaPlayer.seekTo(n);
				return;
			} catch (Exception exception) {
				return;
			}
		}
		this.pendingSeekMs = n;
	}
	
	public void setCornerRadius(float f) {
		this.setClipToOutline(true);
		this.setOutlineProvider(new ViewOutlineProvider() {
			public void getOutline(View view, Outline outline) {
				outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), f);
			}
		});
		this.invalidate();
	}
	
	public void setLoop(boolean bl) {
		this.looping = bl;
		MediaPlayer mediaPlayer = this.player;
		if (mediaPlayer != null) {
			mediaPlayer.setLooping(bl);
		}
	}
	
	public void setMute(boolean bl) {
		this.muted = bl;
		this.applyMute();
	}
	
	public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
		this.preparedListener = onPreparedListener;
	}
	
	/**
* Set listener for when video completion
* @param onCompletionListener Listener untuk menangani event video selesai
*/	
	public void setOnCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
		this.completionListener = onCompletionListener;
	}
	
	public void setVideo(String object) {
		this.pendingSource = object;
		this.prepared = false;
		this.pendingSeekMs = -1;
		try {
			this.player.reset();
			this.player.setLooping(this.looping);
			if (object.startsWith("assets:")) {
				String string = object.substring(7);
				AssetFileDescriptor assetFileDescriptor = this.getContext().getAssets().openFd(string);
				this.player.setDataSource(assetFileDescriptor.getFileDescriptor(), 
				assetFileDescriptor.getStartOffset(), 
				assetFileDescriptor.getLength());
			} else if (object.startsWith("raw:")) {
				String string = object.substring(4);
				int n = this.getResources().getIdentifier(string, "raw", this.getContext().getPackageName());
				AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(n);
				this.player.setDataSource(assetFileDescriptor.getFileDescriptor(), 
				assetFileDescriptor.getStartOffset(), 
				assetFileDescriptor.getLength());
			} else if (object.startsWith("content://")) {
				this.player.setDataSource(this.getContext(), Uri.parse(object));
			} else {
				String filePath = object;
				if (object.startsWith("file://")) {
					filePath = object.substring(7);
				}
				if (filePath.startsWith("/sdcard/")) {
					filePath = filePath.replace("/sdcard/", "/storage/emulated/0/");
				}
				File file = new File(filePath);
				if (!file.exists()) {
					throw new Exception("File not found");
				}
				this.player.setDataSource(filePath);
			}
			if (this.surfaceReady && this.surface != null) {
				this.player.setSurface(this.surface);
			}
			this.player.prepareAsync();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public void onPrepared(MediaPlayer mediaPlayer) {
		this.prepared = true;
		this.applyMute();
		MediaPlayer.OnPreparedListener listener = this.preparedListener;
		if (listener != null) {
			listener.onPrepared(mediaPlayer);
		}
		if (this.pendingSeekMs >= 0) {
			mediaPlayer.seekTo(this.pendingSeekMs);
			this.pendingSeekMs = -1;
		}
		if (this.surfaceReady && this.autoPlay) {
			mediaPlayer.start();
		}
	}
	
	/**
* Callback when video playback is complete
*/	
	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		MediaPlayer.OnCompletionListener listener = this.completionListener;
		if (listener != null) {
			listener.onCompletion(mediaPlayer);
		}
	}
}
