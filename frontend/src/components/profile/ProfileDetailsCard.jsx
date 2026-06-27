import { Card, CardContent } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Mail, Calendar, FileText, BarChart3, Heart, Settings, Lock, Sparkles } from "lucide-react"

export default function ProfileDetailsCard({
  user,
  myBlogsCount,
  totalViews,
  totalLikes,
  formatDate,
  onEditProfile,
  onChangePassword
}) {
  const isAdmin = user?.role === "ADMIN"

  return (
    <Card className="border border-zinc-200 dark:border-zinc-800 shadow-none overflow-hidden rounded-3xl bg-white dark:bg-zinc-950 p-2">
      {/* Premium Cover Banner with bytesblogs Gradient Overlay */}
      <div className="h-28 w-full bg-gradient-to-br from-rose-600 via-rose-500/90 to-amber-500 dark:from-rose-950/80 dark:via-zinc-900 dark:to-zinc-950 relative flex items-center justify-center overflow-hidden rounded-2xl border-b border-border/10">
        {/* Geometric dots grid overlay */}
        <div className="absolute inset-0 bg-[radial-gradient(#ffffff15_1px,transparent_1px)] dark:bg-[radial-gradient(#ffffff08_1px,transparent_1px)] bg-[size:10px_10px] opacity-70" />
        
        {/* Soft decorative radial light source */}
        <div className="absolute top-0 right-0 h-24 w-24 rounded-full bg-white/10 blur-xl pointer-events-none" />
      </div>
      
      <CardContent className="pt-0 pb-6 flex flex-col items-center text-center relative px-4 sm:px-6">
        
        {/* Double-ring gradient avatar */}
        <div className="relative -mt-12 h-24 w-24 rounded-full bg-gradient-to-tr from-rose-500 via-amber-500 to-rose-600 dark:from-rose-500/80 dark:to-amber-500/80 p-1 shadow-lg shadow-rose-500/10 z-10">
          <div className="h-full w-full rounded-full bg-white dark:bg-zinc-900 flex items-center justify-center text-3xl font-black capitalize text-rose-600 dark:text-rose-400 select-none">
            {user?.username?.charAt(0)}
          </div>
        </div>

        {/* Username */}
        <h2 className="text-2xl font-black mt-4 capitalize text-foreground tracking-tight flex items-center gap-1.5 justify-center">
          <span>{user?.username}</span>
          {isAdmin && <Sparkles className="h-4.5 w-4.5 text-amber-500" />}
        </h2>

        {/* Role Badge */}
        <div className="flex mt-2">
          {isAdmin ? (
            <span className="inline-flex items-center gap-1 px-3 py-0.5 rounded-full text-[9px] font-black uppercase tracking-wider bg-rose-500/10 text-rose-600 dark:text-rose-400 border border-rose-500/20">
              {user?.role}
            </span>
          ) : (
            <span className="inline-flex items-center gap-1 px-3 py-0.5 rounded-full text-[9px] font-black uppercase tracking-wider bg-zinc-100 dark:bg-zinc-900 text-zinc-600 dark:text-zinc-400 border border-zinc-200 dark:border-zinc-800">
              {user?.role}
            </span>
          )}
        </div>

        {/* Metrics Grid (Colored Panels) */}
        <div className="grid grid-cols-3 gap-3 w-full mt-6 select-none">
          {/* Articles */}
          <div className="bg-blue-50/40 dark:bg-blue-950/10 border border-blue-100/50 dark:border-blue-950/30 rounded-2xl p-2.5 flex flex-col items-center justify-center transition-all duration-300 hover:scale-[1.03] hover:bg-blue-50/60 dark:hover:bg-blue-950/20">
            <FileText className="h-4.5 w-4.5 text-blue-600 dark:text-blue-400 mb-1.5 shrink-0" />
            <span className="text-sm sm:text-base font-black text-foreground leading-none">{myBlogsCount}</span>
            <span className="text-[8px] text-blue-700/60 dark:text-blue-400/50 uppercase font-black tracking-wider mt-1.5">Articles</span>
          </div>

          {/* Views */}
          <div className="bg-emerald-50/40 dark:bg-emerald-950/10 border border-emerald-100/50 dark:border-emerald-950/30 rounded-2xl p-2.5 flex flex-col items-center justify-center transition-all duration-300 hover:scale-[1.03] hover:bg-emerald-50/60 dark:hover:bg-emerald-950/20">
            <BarChart3 className="h-4.5 w-4.5 text-emerald-600 dark:text-emerald-400 mb-1.5 shrink-0" />
            <span className="text-sm sm:text-base font-black text-foreground leading-none">{totalViews.toLocaleString()}</span>
            <span className="text-[8px] text-emerald-700/60 dark:text-emerald-400/50 uppercase font-black tracking-wider mt-1.5">Views</span>
          </div>

          {/* Likes */}
          <div className="bg-rose-50/40 dark:bg-rose-950/10 border border-rose-100/50 dark:border-rose-950/30 rounded-2xl p-2.5 flex flex-col items-center justify-center transition-all duration-300 hover:scale-[1.03] hover:bg-rose-50/60 dark:hover:bg-rose-950/20">
            <Heart className="h-4.5 w-4.5 text-rose-600 dark:text-rose-400 mb-1.5 shrink-0" />
            <span className="text-sm sm:text-base font-black text-foreground leading-none">{totalLikes.toLocaleString()}</span>
            <span className="text-[8px] text-rose-700/60 dark:text-rose-400/50 uppercase font-black tracking-wider mt-1.5">Likes</span>
          </div>
        </div>

        {/* Credentials detail card block */}
        <div className="w-full mt-6 bg-zinc-50/60 dark:bg-zinc-900/40 border border-zinc-100/80 dark:border-zinc-800/40 rounded-2xl p-4 space-y-3 text-left">
          <div className="flex items-center gap-2.5 text-muted-foreground min-w-0">
            <Mail className="h-4 w-4 shrink-0 text-zinc-400 dark:text-zinc-500" />
            <div className="min-w-0 flex-1">
              <p className="text-[9px] text-zinc-400 dark:text-zinc-500 font-extrabold uppercase tracking-wider leading-none">Email Address</p>
              <p className="text-xs font-semibold text-zinc-800 dark:text-zinc-200 truncate mt-0.5">{user?.email}</p>
            </div>
          </div>
          
          <div className="flex items-center gap-2.5 text-muted-foreground min-w-0">
            <Calendar className="h-4 w-4 shrink-0 text-zinc-400 dark:text-zinc-500" />
            <div className="min-w-0 flex-1">
              <p className="text-[9px] text-zinc-400 dark:text-zinc-500 font-extrabold uppercase tracking-wider leading-none">Joined Date</p>
              <p className="text-xs font-semibold text-zinc-800 dark:text-zinc-200 truncate mt-0.5">{formatDate(user?.createdAt)}</p>
            </div>
          </div>
        </div>

        {/* Dynamic Action Buttons */}
        <div className="w-full mt-6 flex flex-col gap-2 shrink-0">
          <Button 
            onClick={onEditProfile} 
            className="w-full h-10 flex items-center justify-center gap-1.5 rounded-xl bg-gradient-to-r from-rose-600 to-amber-600 hover:from-rose-500 hover:to-amber-500 text-white font-semibold text-xs shadow-md shadow-rose-500/10 hover:shadow-lg hover:shadow-rose-500/20 active:scale-[0.98] transition-all border-none cursor-pointer"
          >
            <Settings className="h-3.5 w-3.5" />
            <span>Edit Profile Info</span>
          </Button>
          <Button 
            onClick={onChangePassword} 
            className="w-full h-10 flex items-center justify-center gap-1.5 rounded-xl border border-zinc-200 dark:border-zinc-800 hover:bg-zinc-50 dark:hover:bg-zinc-900 bg-transparent text-zinc-700 dark:text-zinc-300 font-semibold text-xs transition-colors cursor-pointer"
          >
            <Lock className="h-3.5 w-3.5" />
            <span>Change Password</span>
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
